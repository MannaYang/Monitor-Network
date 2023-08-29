plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

val dingAppKey: String by project
val dingSecretKey: String by project
val dingAgentId: String by project
val dingUsersId: String by project

android {
    namespace = "com.manna.monitor.network"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.manna.monitor.network"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            resValue("string", "DING_AK", dingAppKey)
            resValue("string", "DING_SK", dingSecretKey)
            resValue("string", "DING_AGENT", dingAgentId)
            resValue("string", "DING_USERS", dingUsersId)
        }
        release {
            resValue("string", "DING_AK", dingAppKey)
            resValue("string", "DING_SK", dingSecretKey)
            resValue("string", "DING_AGENT", dingAgentId)
            resValue("string", "DING_USERS", dingUsersId)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}
dependencies {

    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    kapt("com.google.auto.service:auto-service:1.1.1")
    api("com.google.auto.service:auto-service-annotations:1.1.1")
    implementation(project(":stone"))
    implementation(project(":http"))
    implementation(project(":http-export"))
    implementation(project(":room"))
    implementation(project(":room-export"))
    implementation(project(":report"))
    implementation(project(":report-export"))
}