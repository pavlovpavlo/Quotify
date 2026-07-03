plugins {
    AndroidLibrary
    KotlinAndroid
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.core.models"
}

dependencies {
    implementationTests()
}
