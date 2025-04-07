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
    namespace = "${AppConfig.applicationId}.feature.onboarding"
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