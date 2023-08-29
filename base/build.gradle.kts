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
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")

    kapt("com.google.auto.service:auto-service:1.1.1")
    api("com.google.auto.service:auto-service-annotations:1.1.1")

    api("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    api("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    api("com.squareup.okio:okio:3.0.0")

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    api("androidx.core:core-ktx:1.10.0")
    api("androidx.fragment:fragment-ktx:1.5.6")
    api("androidx.activity:activity-ktx:1.7.0")

    api("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    api("androidx.lifecycle:lifecycle-livedata-core-ktx:2.6.1")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    api("androidx.lifecycle:lifecycle-reactivestreams-ktx:2.6.1")
    api("androidx.lifecycle:lifecycle-service:2.6.1")
    api("androidx.lifecycle:lifecycle-process:2.6.1")
}