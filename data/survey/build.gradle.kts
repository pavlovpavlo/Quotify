plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    KotlinSerialization
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.data.survey"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:firebase"))
    implementation(project(":core:datastore"))
    implementationFirebase()
    implementationFirebaseRemoteConfig()
    implementationDatastore()
    implementationSerialization()
    implementationCoroutines()
    implementationHilt()
    implementationLogs()
    implementationTests()
}
