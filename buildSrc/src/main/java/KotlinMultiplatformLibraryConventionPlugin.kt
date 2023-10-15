import build.extensions.configureAndroidKotlin
import build.extensions.configureKotlinMultiplatform
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@OptIn(ExperimentalKotlinGradlePluginApi::class)
class KotlinMultiplatformLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
//                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                configureAndroidKotlin(this)
            }
            extensions.configure<KotlinMultiplatformExtension> {
                configureKotlinMultiplatform(this)
            }
        }
    }
}
