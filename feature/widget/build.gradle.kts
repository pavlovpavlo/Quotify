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
    namespace = "${AppConfig.applicationId}.feature.widget"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":domain"))
    implementation(project(":design-systems"))

    implementationAndroidX()
    implementationCompose()
    implementationGlance()
    implementationHilt()
    implementationNavigation()
    implementationSerialization()
    implementationLogs()
    implementationTests()
}
