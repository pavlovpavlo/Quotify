plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.data.config"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:firebase"))
    implementationFirebaseRemoteConfig()
    implementationCoroutines()
    implementationHilt()
}
