plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.core.billing"
}

dependencies {
    api(project(":core:ui"))
    api(project(":core:models"))
    implementationTests()
    implementationHilt()
    implementationLogs()

    implementationBilling()
}
