import build.extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AndroidRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KaptExtension> {
                arguments {
                    arg("room.schemaLocation", "$projectDir/schemas")
                }
            }

            val libs = versionCatalog()
            dependencies {
                "implementation"(libs.findBundle("room").get())
                "ksp"(libs.findBundle("room-ksp").get())
            }
        }
    }
}
