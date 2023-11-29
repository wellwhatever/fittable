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
    // TODO solve this!
//    kotlin.ksp,kotlin.serialization
//    id(libs.plugins.android.application.get().pluginId)
}
