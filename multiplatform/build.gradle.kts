// TODO extract this to convention plugins

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "mulfiplatform"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.koin)
            }
        }
        val commonTest by getting {
            dependencies {
                // test libraries
            }
        }
    }
}

android {
    namespace = "cz.cvut.fit.fittable.multiplatform"
    compileSdk = libs.versions.compileSdk.get().toString().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toString().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
