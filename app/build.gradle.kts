plugins {
    id("fittable.android.application")
    id("fittable.android.application.compose")
    id("fittable.android.firebase")
    id("fittable.android.navigation")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "cz.cvut.fit.fittable"

    defaultConfig {
        applicationId = "cz.cvut.fit.fittable"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["appAuthRedirectScheme"] = "fit-timetable"
    }

    signingConfigs {
        create("release") {
            // TODO Extract to env variables
            keyAlias = "key-fittable"
            keyPassword = "asdqlwrhjljdfn12312jl34n"
            storeFile = File("$rootDir/keys/keystore.jks")
            storePassword = "asdqlwrhjljdfn12312jl34n"
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/versions/9/previous-compilation-data.bin")
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    with(libs) {
        implementation(androidCore)
        implementation(appAuth)
        implementation(android.gms)
        implementation(bundles.koin)
        implementation(bundles.compose)
        implementation(datastore)
        implementation(kotlin.serialization)
        implementation(libs.kotlin.coroutinesAndroid)
        implementation(libs.kotlin.datetime)
        implementation(libs.calendar)
        implementation("me.onebone:toolbar-compose:2.3.5")
    }

    implementation(project(":shared"))
}
