plugins {
    AndroidLibrary
    KotlinAndroid
    Compose
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.feature.common"
}

dependencies {
    implementation(project(":design-systems"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:analytics"))
    implementation(project(":domain"))
    implementationAndroidX()
    implementationCompose()
    implementationHilt()
    implementationCoroutines()
    implementationTests()
}
