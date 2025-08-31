pipeline {
  agent any
  options { ansiColor('xterm'); timeout(time: 45, unit: 'MINUTES') }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build') {
      steps { sh './gradlew clean assembleDebug' }
    }
    stage('Lint') {
      steps { sh './gradlew lint' }
    }
    stage('Unit Tests') {
      steps { sh './gradlew testDebugUnitTest' }
    }
  }

  post {
    always {
      junit '**/test-results/testDebugUnitTest/*.xml'
      archiveArtifacts artifacts: '**/build/outputs/**/*.apk', fingerprint: true
    }
  }
}
