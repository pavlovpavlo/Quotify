plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.data.billing"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:billing"))
    implementation(project(":core:network"))
    implementation(project(":core:firebase"))
    implementation(project(":core:models"))
    implementationFirebase()
    implementationCoroutines()
    implementationHilt()
    implementationLogs()
}
