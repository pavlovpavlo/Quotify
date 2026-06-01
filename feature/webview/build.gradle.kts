plugins {
    AndroidLibrary
    KotlinAndroid
    Compose
    Ktlint
    KotlinSerialization
}

android {
    namespace = "${AppConfig.applicationId}.feature.webview"
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
    implementationTests()
}
