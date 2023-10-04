import build.extensions.versionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeNavigationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            val libs = versionCatalog()
            dependencies {
                "implementation"(libs.findBundle("navigation").get())
                "ksp"(libs.findBundle("navigation-ksp").get())
            }
        }
    }
}
