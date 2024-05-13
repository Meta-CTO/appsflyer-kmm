import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream
import java.io.FileOutputStream

plugins {
    id(Plugins.androidLibrary)
    kotlin(Plugins.Kotlin.multiplatform)
    kotlin(Plugins.Kotlin.serialization) version Versions.Kotlin.KOTLIN
    kotlin(Plugins.cocoapods)
    id(Plugins.mavenPublish)
    id(Plugins.signing)
}

val versionProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "versions.properties")))
}

val currentVersion = versionProperties.getProperty("PUBLISH_VERSION") as String
val libName = "appsflyer"

version = currentVersion
group = "com.metacto"

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        publishLibraryVariants("debug", "release")
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the AppsFlyer Module"
        homepage = "Link to the AppsFlyer Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")

        pod(Libs.APPS_FLYER_POD) {
            version = Versions.Libs.APPS_FLYER_POD
            moduleName = Libs.APPS_FLYER_MODULE_NAME
        }

        framework {
            baseName = libName
            isStatic = true
        }
    }

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework(libName) {
            baseName = libName
            xcf.add(this)
            isStatic = true
        }
    }

    metadata {
        compilations.matching { it.name == "iosMain" }.all {
            compileTaskProvider.configure { enabled = false }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(Libs.COROUTINES)
        }
        commonTest.dependencies {

        }

        androidMain.dependencies {
            api(Libs.APPS_FLYER_LIB)
            api(Libs.INSTALL_REFERRER)
        }

        iosMain.dependencies {

        }
    }

    task("testClasses")
}

android {
    namespace = "com.metacto.kmm.appsflyer"
    compileSdk = Versions.Android.COMPILE_SDK
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.Android.MIN_SDK
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

publishing {
    repositories {
        val localProperties = gradleLocalProperties(rootDir, providers)
        var publishUserRepo = localProperties.getProperty("PUBLISH_REPO_USER")
        var publishTokenRepo = localProperties.getProperty("PUBLISH_REPO_TOKEN")

        if (publishUserRepo.isNullOrEmpty()) {
            publishUserRepo = ""
            localProperties.setProperty("PUBLISH_REPO_USER", publishUserRepo)
        }

        if (publishTokenRepo.isNullOrEmpty()) {
            publishTokenRepo = ""
            localProperties.setProperty("PUBLISH_REPO_TOKEN", publishTokenRepo)
        }

        if (publishUserRepo.isEmpty() || publishTokenRepo.isEmpty()) {
            localProperties.store(
                FileOutputStream(File(rootDir, "local.properties")), null
            )
        }

        repositories {
            maven("https://maven.pkg.github.com/Meta-CTO/appsflyer-kmm") {
                name = "Github"
                credentials {
                    username = publishUserRepo
                    password = publishTokenRepo
                }
            }
        }
    }
}
