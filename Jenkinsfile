pipeline{
    agent any 

    tools {
        maven "maven-3911"
    }

    environment {
        APP_NAME = "java-servlet"
        DEPLOY_PATH = "/home/osboxes/server/dev/webapps"
        MVANE_SETTINGS_ID = "f4783756-8786-49db-a137-0541f27321d7"
    }

    options {
        timestamps() 
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    stages{
        stage("Compile") {
            steps{
                sh "mvn clean compile"
            }
        }

        stage("Unit Tests"){
            steps{
                sh "mvn test"
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage("SonarQube Analysis"){
            steps {
                withSonarQubeEnv('sonarqube-scanner') {
                    sh """mvn sonar:sonar \
                        -Dsonar.projectKey=${APP_NAME}\
                        -Dsonar.projectName='${APP_NAME}' """
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage("Build") {
            steps{
                sh "mvn package -DskipTests"
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage("OWASP Dependency Check"){
            steps{
                dependencyCheck additionalArguments: '--scan target/', odcInstallation: 'dependency-check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        stage("Push To Nexus"){
            steps{
                configFileProvider([configFile(fileId: env.MVANE_SETTINGS_ID, variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS  deploy -DskipTests"
                }
            }
        }

        stage("Deploy") {
            steps{
                sh "cp target/*.war ${DEPLOY_PATH}/${APP_NAME}.war"
            }
        }

        stage("Triger QA Pipeline"){
            steps{
                build job: 'Java-servlet-deploy-on-QA',
                wait : false,
                parameters: [string(name: 'BUILD_ID', value: "${env.BUILD_ID}")]
            }
        }
    }
}