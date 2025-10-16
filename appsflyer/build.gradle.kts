import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI

plugins {
    id(Plugins.androidLibrary)
    kotlin(Plugins.Kotlin.multiplatform)
    kotlin(Plugins.Kotlin.serialization) version Versions.Kotlin.KOTLIN
    id(Plugins.SPM_4_KMP) version Versions.Plugins.SPM_4_KMP
    id(Plugins.mavenPublish)
    id(Plugins.signing)

}

val versionProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "versions.properties")))
}

val currentVersion = versionProperties.getProperty("PUBLISH_VERSION") as String
val libName = "appsFlyer"
val cinteropName = "AppsFlyerSDK"

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

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework(libName) {
            baseName = libName
            xcf.add(this)
        }
        it.compilations {
            val main by getting {
                cinterops.create(cinteropName)
            }
        }
    }

    swiftPackageConfig {
        create(cinteropName) {
            dependency {
                remoteBinary(
                    url = URI("https://github.com/AppsFlyerSDK/AppsFlyerFramework/releases/download/6.16.2/AppsFlyerLib-Dynamic-SPM.xcframework.zip"),
                    exportToKotlin = true,
                    packageName = "AppsFlyerLib",
                    checksum = "6ce9bf6da08f85f6eafac2520ef0d0579d0724b3b2200cb46dcc18993cd02608"
                )
            }
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
            implementation(Libs.SERIALIZATION_JSON)
        }
        commonTest.dependencies {
            implementation(Libs.KOTLIN_TEST)
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
