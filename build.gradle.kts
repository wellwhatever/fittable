import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    with(libs.plugins) {
        listOf(
            android.application,
            android.library,
            kotlin.android,
            kotlin.parcelize,
        ).forEach {
            id(it.get().pluginId) apply false
        }
    }
    id("com.google.gms.google-services") version libs.versions.gms apply false
    id("com.google.devtools.ksp") version libs.versions.ksp apply false
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin apply false
    id("app.cash.sqldelight") version libs.versions.sqlDelightGradle apply false
    id("co.touchlab.skie") version libs.versions.skie apply false
    id("io.gitlab.arturbosch.detekt") version libs.versions.detekt
}

detekt {
    source.from(files(rootProject.rootDir))
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

tasks {
    fun SourceTask.config() {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    }
    withType<DetektCreateBaselineTask>().configureEach {
        config()
    }
    withType<Detekt>().configureEach {
        config()

        reports {
            sarif.required.set(true)
        }
    }
}
