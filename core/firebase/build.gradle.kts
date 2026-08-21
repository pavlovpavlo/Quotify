plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.core.firebase"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementationFirebase()
    implementationFirebaseRemoteConfig()
    implementationCoroutines()
    implementationHilt()
}
