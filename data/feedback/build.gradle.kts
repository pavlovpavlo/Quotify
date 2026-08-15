plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.data.feedback"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:firebase"))
    implementation(project(":core:datastore"))
    implementationFirebase()
    implementationDatastore()
    implementationCoroutines()
    implementationHilt()
}
