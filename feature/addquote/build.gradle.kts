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
    namespace = "${AppConfig.applicationId}.feature.addquote"
}


dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))
    implementation(project(":design-systems"))
    implementation(project(":domain"))
    implementationAndroidX()
    implementationCompose()
    implementationCoroutines()
    implementationNavigation()
    implementationCamera()
    implementationSerialization()
    implementationLogs()
    implementationHilt()
    implementationTests()
}
