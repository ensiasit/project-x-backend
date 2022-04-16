pipeline {
    environment {
        imageName = "ensiasit/project-x-api"
        imageTag = "latest"
        registryCredentials = "a6962ad2-5859-449f-84ae-b7252d913034"
    }

    agent any

    stages {
        stage('Build image') {
            steps {
                script {
                    dockerImage = docker.build imageName
                }
            }
        }

        stage('Push image') {
            steps {
                script {
                    docker.withRegistry('', registryCredentials) {
                        dockerImage.push(imageTag)
                    }
                }
            }
        }

        stage('Clean image') {
            steps {
                sh "docker image rm $imageName:$imageTag"
            }
        }
    }
}