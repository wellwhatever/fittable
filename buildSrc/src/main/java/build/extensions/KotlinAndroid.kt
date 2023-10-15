package build.extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureAndroidKotlin(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    val libs = versionCatalog()
    commonExtension.apply {
        compileSdk = libs.findVersion("compileSdk").get().toString().toInt()
        defaultConfig {
            minSdk = libs.findVersion("minSdk").get().toString().toInt()
        }
        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
            )
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("desugaring").get())
        }
    }
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
fun Project.configureKotlinMultiplatform(
    multiplatformExtension: KotlinMultiplatformExtension,
) {
    val libs = versionCatalog()
    multiplatformExtension.apply {
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
//        sourceSets {
//            val commonMain by getting {
//                dependencies {
//                    // put your multiplatform dependencies here
//                }
//            }
//            val commonTest by getting {
//                dependencies {
////                implementation(libs.kotlin.test)
//                }
//            }
//        }
    }
}
