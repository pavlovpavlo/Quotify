plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.data.voice"
}

dependencies {
    implementation(project(":domain"))
    implementationCoroutines()
    implementationHilt()
    implementationLogs()
}
