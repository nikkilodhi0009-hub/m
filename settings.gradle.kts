pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "android10-hook-framework"

include(
    ":app",
    ":core",
    ":api",
    ":common",
    ":hooks",
    ":manager",
    ":service",
    ":bridge",
    ":daemon",
    ":example-module"
)
