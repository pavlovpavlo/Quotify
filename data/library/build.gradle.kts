plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.data.library"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:firebase"))
    implementation(project(":core:models"))
    implementationFirebase()
    implementationCoroutines()
    implementationHilt()
    implementationTests()
}
