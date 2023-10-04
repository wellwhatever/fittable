plugins {
    id("fittable.android.application")
    id("fittable.android.application.compose")
    id("fittable.android.firebase")
    id("fittable.android.navigation")
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
    }

    signingConfigs {
        create("release") {
            // TODO Extract to env variables
            keyAlias = "key"
            keyPassword = "keypassword"
            storeFile = File("$rootDir/keys/keystore.jks")
            storePassword = "keypassword"
        }
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
        implementation(bundles.compose)
    }
}
