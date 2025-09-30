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