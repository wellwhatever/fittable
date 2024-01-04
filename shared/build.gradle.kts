import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("app.cash.sqldelight")
    id("co.touchlab.skie")
}

repositories {
    google()
    mavenCentral()
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
                implementation(libs.sqlDelight.common)
                implementation(libs.sqlDelight.coroutines)
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
                implementation(libs.sqlDelight.android)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.bundles.unitTesting)
            }
            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
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
                implementation(libs.sqlDelight.ios)
            }
        }
    }

    targets.filterIsInstance<KotlinNativeTarget>().forEach{
        it.binaries.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.Framework>()
            .forEach { lib ->
                lib.isStatic = false
                lib.linkerOpts.add("-lsqlite3")
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
dependencies {
    implementation(libs.animation.graphics.android)
    testImplementation(libs.kotest.runner)
}

sqldelight {
    databases {
        create("TimetableDatabase") {
            packageName.set("cz.cvut.fit.fittable")
            generateAsync.set(true)
        }
    }
    linkSqlite.set(true)
}
