plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.1.0"
}

android {
    namespace = "com.example.cathycatering"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cathycatering"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.picasso)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation(platform(libs.okhttp.bom))
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.serialization.json) // Or latest version
    implementation (libs.androidx.recyclerview)
    implementation (libs.gson)
    implementation (libs.play.services.wallet)
    implementation (libs.kotlinx.serialization.json.v140)
    implementation (libs.androidx.lifecycle.livedata.ktx)
//    implementation(libs.kotlinx.serialization.json.v173)

// define any required OkHttp artifacts without version

    implementation(libs.okhttp)

    implementation(libs.logging.interceptor)
    implementation(libs.okhttp3.logging.interceptor)
}