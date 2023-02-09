pipeline {

    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    agent any

    tools {
        maven 'maven-3.8.7'
    }
    stages {
        stage('Code Compilation') {
            steps {
                echo 'code compilation is starting'
                sh 'mvn clean compile'
				echo 'code compilation is completed'
            }
        }/**
        stage('Sonarqube') {
                    environment {
                        scannerHome = tool 'qube'
                    }
                    steps {
                        withSonarQubeEnv('sonar-server') {
                            sh "${scannerHome}/bin/sonar-scanner"
                            sh 'mvn sonar:sonar'
                        }
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }**/
        stage('Code Package') {
            steps {
                echo 'code packing is starting'
                sh 'mvn clean package'
				echo 'code packing is completed'
            }
        }
        stage('Building  Docker Image') {
            steps {
                echo 'Starting Building Docker Image'
                sh 'docker build -t jayantmankar/flipkart-ms .'
                sh 'docker build -t flipkart-ms .'
                echo 'Completed  Building Docker Image'
            }
        }
        stage('Docker Image Scanning') {
                    steps {
                        script{
                        echo 'Docker Image Scanning Started'
                        snykSecurity severity: 'critical', snykInstallation: 'snyk', snykTokenId: 'snyk'
                        def variable = sh(
                                     script: 'snyk container test jayantmankar/flipkart-ms --severity-threshold-critical',
                                     returnStatus: true)

                            echo "error code = ${variable}"
                            if (variable !=0) {
                               echo "Alert for vulnerability found"
                            }
                        }
                    }
                }
        stage(' Docker push to Docker Hub') {
           steps {
              script {
                 withCredentials([string(credentialsId: 'jayantmankar', variable: 'jayantmankar')]){
                 sh 'docker login docker.io -u jayantmankar -p ${jayantmankar}'
                 echo "Push Docker Image to DockerHub : In Progress"
                 sh 'docker push jayantmankar/flipkart-ms:latest'
                 echo "Push Docker Image to DockerHub : Done"
                 sh 'whoami'
                 }
              }
            }
        }/**
        stage(' Docker Image Push to Amazon ECR') {
           steps {
              script {
                 withDockerRegistry([credentialsId:'ecr:ap-south-1:ECR-Creds', url:"https://277543575200.dkr.ecr.ap-south-1.amazonaws.com"]){
                 sh """
                 echo "List the docker images present in local"
                 docker images
                 echo "Tagging the Docker Image: In Progress"
                 docker tag flipkart-ms:latest 277543575200.dkr.ecr.ap-south-1.amazonaws.com/flipkart-ms:latest
                 echo "Tagging the Docker Image: Completed"
                 echo "Push Docker Image to ECR : In Progress"
                 docker push 277543575200.dkr.ecr.ap-south-1.amazonaws.com/flipkart-ms:latest
                 echo "Push Docker Image to ECR : Completed"
                 """
                 }
              }
           }
        }
        stage('Upload the docker Image to Nexus') {
           steps {
              script {
              withCredentials([usernamePassword(credentialsId: 'Nexuscreds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]){
              sh 'docker login http://3.6.89.35:8085/repository/flipkart-ms/ -u admin -p ${PASSWORD}'
              echo "Push Docker Image to Nexus : In Progress"
              sh 'docker tag flipkart-ms 3.6.89.35:8085/flipkart-ms:latest'
              sh 'docker push 3.6.89.35:8085/flipkart-ms'
              echo "Push Docker Image to Nexus : Completed"
                 }
              }
           }
        }**/
         stage('Delete Docker Images from Jenkins ') {
                    steps {
                        echo 'Docker Image Scanning Started'
                        sh 'docker rmi 277543575200.dkr.ecr.ap-south-1.amazonaws.com/flipkart-ms:latest'
                        sh 'sleep 2'
                        sh 'docker rmi jayantmankar/flipkart-ms:latest'
                        sh 'sleep 2'
                        sh 'docker rmi flipkart-ms:latest'
                        sh 'sleep 2'
                        sh 'docker rmi tomcat:8.0.51-jre8-alpine'
                        sh 'sleep 2'
                        echo 'Docker Image Scanning removing from local completed'
                        echo 'docker images on linux'
                        sh 'docker images'
                    }
                }
    }
}

