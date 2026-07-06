plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.core.firebase"
}

dependencies {
    implementationFirebase()
    implementationCoroutines()
    implementationHilt()
}
