pipeline{
    agent any 

    tools {
        maven "maven-3911"
    }

    stages{
        stage("build"){
            steps{
                sh "mvn clean package"
            }
        }

        stage("deploy"){
            steps{
                sh "cp target/*.war /home/osboxes/dev-server/webapps/java-servlet.war"
            }
        }
    }
}