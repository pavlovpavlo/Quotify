plugins {
    AndroidLibrary
    KotlinAndroid
    Compose
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.feature.common"
}

dependencies {
    implementation(project(":design-systems"))
    implementation(project(":core:navigation"))
    implementationAndroidX()
    implementationCompose()
    implementationTests()
}
