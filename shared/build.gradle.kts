import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

// TODO extract this to convention plugins

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }

    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.multiplatform)
                implementation(libs.datastore)
                implementation(libs.bundles.ktor.multiplatform)
                implementation(libs.kotlin.coroutinesCore)
                implementation(libs.kotlin.serialization)
                implementation(libs.kotlin.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                // test libraries
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.android)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.ios)
            }
        }
    }
}

android {
    namespace = "cz.cvut.fit.fittable.shared"
    compileSdk = libs.versions.compileSdk.get().toString().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toString().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
