pipeline{
    agent any 

    tools {
        maven "maven-3911"
    }

    stages{
        stage("build") {
            steps{
                sh "mvn clean package"
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage("test"){
            steps{
                sh "mvn test"
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage("sonarqube-analysis"){
            environment {
                scannerHome = tool 'SonarScanner' // name from Jenkins tools config
            }
            steps {
                withSonarQubeEnv('sonarqube-scanner') {
                    sh '''${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=myproject \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.host.url=http://192.168.1.10:9000 \
                        -Dsonar.login=$SONAR_AUTH_TOKEN'''
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage("deploy") {
            steps{
                sh "cp target/*.war /home/osboxes/server/dev/webapps/java-servlet.war"
            }
        }

        stage("triger-QA-pipeline"){
            steps{
                build job: 'Java-servlet-deploy-on-QA',
                wait : false
            }
        }
    }
}