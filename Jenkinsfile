pipeline{
    agent any 

    tools {
        maven "maven-3911"
    }

    environment {
        DEPLOY_PATH = "/home/osboxes/server/dev/webapps"
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
                    sh '''mvn sonar:sonar \
                        -Dsonar.projectKey=myproject \
                        -Dsonar.projectName='myproject' '''
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
                sh "mvn clean package -DskipTests"
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage("Push To Nexus"){
            steps{
                configFileProvider([configFile(fileId: 'f4783756-8786-49db-a137-0541f27321d7', variable: 'MAVEN_SETTINGS')]) {
                    sh "mvn -s $MAVEN_SETTINGS clean deploy -DskipTests"
                }
            }
        }

        stage("Deploy") {
            steps{
                sh "cp target/*.war $DEPLOY_PATH/java-servlet.war"
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