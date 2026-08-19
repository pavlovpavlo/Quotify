plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.core.analytics"
}

dependencies {
    implementationFirebaseAnalytics()
    implementationCoroutines()
    implementationLogs()
    implementationHilt()
}
