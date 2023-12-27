plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.firebase.gradlePlugin)
}

group = "cz.cvut.fit.fittable.buildscr"

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "fittable.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "fittable.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "fittable.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "fittable.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidFirebase") {
            id = "fittable.android.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidNavigationCompose") {
            id = "fittable.android.navigation"
            implementationClass = "AndroidComposeNavigationConventionPlugin"
        }
        register("kotlinMultiplatform") {
            id = "fittable.kotlin.multiplatform.library"
            implementationClass = "KotlinMultiplatformLibraryConventionPlugin"
        }
    }
}
