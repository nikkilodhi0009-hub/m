plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.example.framework.daemon"
    compileSdk = 33

    defaultConfig {
        minSdk = 29
        targetSdk = 29
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
    implementation(project(":common"))
    implementation(project(":service"))
}
