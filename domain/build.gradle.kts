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
    api(project(":core:models"))
    implementationHilt()
    implementationCoroutines()
    implementationSerialization()
    implementationLogs()
    implementationTests()
}
