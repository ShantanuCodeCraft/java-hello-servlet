pipeline{
    agent any 

    tools {
        maven "maven-3911"
    }

    stages{
        stage("build"){
            steps{
                sh "mvn clean package"
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage("deploy"){
            steps{
                sh "cp target/*.war /home/osboxes/dev-server/webapps/java-servlet.war"
            }
        }
    }


}