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
    namespace = "${AppConfig.applicationId}.feature.survey"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))
    implementation(project(":domain"))
    implementation(project(":design-systems"))

    implementationAndroidX()
    implementationCompose()
    implementationHilt()
    implementationNavigation()
    implementationSerialization()
    implementationLogs()
    implementationTests()
}
