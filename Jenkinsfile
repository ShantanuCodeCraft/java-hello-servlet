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
                sh "cp target/*.war /home/osboxes/apache-tomcat-10.1.46/webapps/java-servlet.war"
            }
        }
    }
}