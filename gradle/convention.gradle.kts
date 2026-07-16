import com.android.build.gradle.LibraryExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure

fun configureAndroidModule(project: Project) {
    project.plugins.apply("kotlin-android")
    project.configure<LibraryExtension> {
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
}

fun Project.configureAndroidCommon() {
    plugins.apply("com.android.library")
    configureAndroidModule(this)
}
