plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
    KotlinSerialization
}

android {
    namespace = "${AppConfig.applicationId}.data.settings"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:datastore"))
    implementationDatastore()
    implementationCoroutines()
    implementationHilt()
    implementationSerialization()
}
