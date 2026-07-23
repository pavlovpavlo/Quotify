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
    namespace = "${AppConfig.applicationId}.feature.entitydetails"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":design-systems"))
    implementation(project(":domain"))
    implementation(project(":feature:common"))
    implementationAndroidX()
    implementationCompose()
    implementationCoroutines()
    implementationNavigation()
    implementationSerialization()
    implementationLogs()
    implementationHilt()
    implementationTests()
}
