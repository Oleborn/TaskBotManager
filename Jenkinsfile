pipeline {
    agent any

    environment {
        DB_PASSWORD = credentials('db_password') // Получаем пароль из Jenkins Credentials
        BOT_TOKEN = credentials('bot_token') // Получаем токен из Jenkins Credentials
    }


    tools {
        maven 'Maven 3.9.9'
    }

    stage('Debug Environment Variables') {
        steps {
            sh 'env | grep DB_'
            sh 'env | grep BOT_'
        }
    }

    stages {

        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Oleborn/TaskBotManager.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Compose Down (Cleanup)') {
            steps {
                sh '''
                docker-compose down --remove-orphans
                '''
                // Удаление контейнеров, которые не используются
                sh '''
                docker container prune -f
                '''
            }
        }

        stage('Remove Old Application Images') {
            steps {
                sh '''
                docker images spring-boot-app --format "{{.ID}}" | tail -n +2 | xargs -r docker rmi
                '''
            }
        }

        stage('Docker Compose Build') {
            steps {
                sh 'docker-compose build'
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh '''
                docker-compose up -d
                '''
            }
        }
    }
}