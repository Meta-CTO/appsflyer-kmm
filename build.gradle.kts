buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
        maven(url = "https://jitpack.io")
    }
    dependencies {
//        classpath(Build.kotlinGradlePlugin)
//        classpath(Build.buildTools)

        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
        classpath("com.android.tools.build:gradle:8.4.0")

    }
}

