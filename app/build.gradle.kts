plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.hookframework"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.hookframework"
        minSdk = 29
        targetSdk = 29
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(project(":core"))
    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":hooks"))
    implementation(project(":manager"))
    implementation(project(":service"))
    implementation(project(":bridge"))
    implementation(project(":daemon"))
    implementation(project(":example-module"))

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
}
