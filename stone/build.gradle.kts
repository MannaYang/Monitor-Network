plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.manna.monitor.base"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")

    api("com.google.code.gson:gson:2.9.0")

    kapt("com.google.auto.service:auto-service:1.1.1")
    api("com.google.auto.service:auto-service-annotations:1.1.1")

    api("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    api("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    api("com.squareup.okio:okio:3.0.0")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    api("androidx.datastore:datastore-preferences:1.0.0")
    api("androidx.preference:preference-ktx:1.2.0")
}