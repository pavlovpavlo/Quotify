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
    implementationSerialization()
    implementationLogs()
    implementationTests()
}