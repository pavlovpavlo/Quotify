plugins {
    AndroidLibrary
    KotlinAndroid
    Compose
    KotlinKapt
    Hilt
    Ktlint
    KotlinSerialization
}

android {
    namespace = "${AppConfig.applicationId}.feature.home"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":design-systems"))
    implementationAndroidX()
    implementationCompose()
    implementationCoroutines()
    implementationNavigation()
    implementationSerialization()
    implementationLogs()
    implementationHilt()
    implementationTests()
}