pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('SonarScan') {
            steps {
                withSonarQubeEnv(installationName: 'sq1', credentialsId: 'sonarqube-secret') {
                    sh './gradlew sonarqube'
                }
            }
        }

        stage('QualityGate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                sh './gradlew war'
            }
        }

        stage ('Deploy') {
            steps {
                echo 'Deploying...'
                sh 'ls'
                dir('web/build/libs') {
                    script {
                        deploy adapters: [tomcat9(credentialsId: 'tomcat-deployer', path: '', url: 'http://localhost:8080')], contextPath: '/app', onFailure: false, war: 'web-0.0.1-SNAPSHOT-plain.war'
                    }
                }
            }
        }
    }
}
