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