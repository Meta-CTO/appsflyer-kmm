buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath(Build.kotlinGradlePlugin)
        classpath(Build.buildTools)
    }
}

