plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.data.scan"
}

dependencies {
    implementation(project(":domain"))
    implementationCoroutines()
    implementationHilt()
    implementationLogs()
    implementationFirebaseAi()
}
