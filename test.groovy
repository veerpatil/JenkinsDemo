pipeline {
    agent any

    stages {
        stage('Get Source Code') {
            steps {
                git credentialsId: '291a3c49-fa9f-409c-ad87-cae9c219fcf6', url: 'https://github.com/veerpatil/JenkinsDemo.git'
                echo 'Hello World'
            }
        }
        stage('Build code')
                {
                    steps
                            {
                                bat script: 'mvn compile'
                            }
                }
        stage('Run Test')
                {
                    steps
                            {
                                bat script: 'mvn test -Dbrowser=localchrome'
                            }
                }
        stage('Publish Report')
                {
                    steps
                            {
                                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: '', reportFiles: 'target/surefire-reports/Extent*.html', reportName: 'Pipeline', reportTitles: ''])
                            }
                }
    }
}
