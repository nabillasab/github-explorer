import java.util.Properties
import org.gradle.testing.jacoco.tasks.JacocoReport

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
val apiKey = localProperties.getProperty("github_auth_token") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.jacoco)

}

android {
    namespace = "com.example.githubuser"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.githubuser"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/NOTICE.md",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
        }
    }
}

// ------------ JaCoCo (unit tests & androidTest) ------------
tasks.register<JacocoReport>("jacocoMergedReport") {
    group = "verification"
    description = "Combined JaCoCo report for unit + androidTest."

    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")

    val coverageExclusions = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
        "**/*Test*.*", "android/**/*.*",
        "**/*Application*.*", "**/*Hilt*.*", "**/*_HiltModules*.*",
        "**/*_Factory.*", "**/*_MembersInjector.*", "**/*_Impl*.*",
        "**/*_GeneratedInjector.*"
    )

    val javaClasses = fileTree("$buildDir/intermediates/javac/debug/classes") { exclude(coverageExclusions) }
    val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/debug") { exclude(coverageExclusions) }
    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))

    val execFiles = files(
        fileTree("$buildDir/jacoco") { include("testDebugUnitTest.exec") },
        fileTree("$buildDir/outputs/unit_test_code_coverage") { include("**/testDebugUnitTest.exec") },
        fileTree("$buildDir/outputs/code_coverage") { include("**/*.ec") }
    ).filter { it.exists() && it.length() > 0 }

    executionData.setFrom(execFiles)

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

dependencies {

    // Coil
    implementation(libs.coil.kt.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.gson)
    implementation(libs.okhttp.logging)

    // Paging
    implementation(libs.paging.compose)
    implementation(libs.paging.runtime)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Unit Testing
    testImplementation(libs.kotlinx.coroutine.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.paging.common)
    testImplementation(libs.junit)
    testImplementation(libs.hilt.android.testing)

    // androidtest
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.rules)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.paging.testing)
    androidTestImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.kotlinx.coroutine.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.paging.common)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
