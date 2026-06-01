plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    KotlinParcelize
    KotlinSerialization
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.domain"
}

dependencies {
    implementationHilt()
    implementationCoroutines()
    implementationSerialization()
    implementationLogs()
    implementationTests()
}
