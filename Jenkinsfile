pipeline {
  agent any
  environment {
    ANDROID_SDK_ROOT = '/var/jenkins_home/android-sdk'
    PATH = "${env.ANDROID_SDK_ROOT}/platform-tools:${env.ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${env.PATH}"
  }

  stages {
    stage('Install Android SDK (first run only)') {
      steps {
        sh '''
          set -e
          if [ ! -d "$ANDROID_SDK_ROOT/cmdline-tools/latest" ]; then
            echo "==> Installing Android SDK..."
            mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools"
            cd /tmp
            curl -sSLo cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
            unzip -q cmdline-tools.zip -d "$ANDROID_SDK_ROOT/cmdline-tools"
            mv "$ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools" "$ANDROID_SDK_ROOT/cmdline-tools/latest"

            yes | "$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager" --licenses >/dev/null
            "$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager" \
              "platform-tools" \
              "platforms;android-35" \
              "build-tools;35.0.0"
          else
            echo "==> Android SDK already installed. Skipping."
          fi
        '''
      }
    }

    stage('Prepare Gradle') {
      steps {
        // Point Gradle to the installed SDK
        sh 'echo "sdk.dir=${ANDROID_SDK_ROOT}" > local.properties'
        sh 'chmod +x gradlew'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew clean assembleDebug'
      }
    }

    stage('Tests & Coverage') {
      steps {
        //run locally
        //sh './gradlew clean lint testDebugUnitTest connectedDebugAndroidTest jacocoMergedReport --no-build-cache --rerun-tasks'
        //run on Jenkins - temporary skipping connectedDebugAndroidTest
        sh './gradlew clean lint testDebugUnitTest jacocoMergedReport --no-build-cache --rerun-tasks'
      }
    }
  }

  post {
    always {
      junit '**/build/test-results/**/*.xml'
      archiveArtifacts artifacts: '**/build/reports/jacoco/**', fingerprint: true
      archiveArtifacts artifacts: '**/build/outputs/**/*.apk', fingerprint: true
    }
    success {
      jacoco(
         execPattern: '**/jacocoMergedReport.exec',
         classPattern: '**/classes',
         sourcePattern: '**/src/main/java'
         )
      }
  }
}
