plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.example.framework.common"
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
    testImplementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
}
