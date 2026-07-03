plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.core.datastore"
}

dependencies {
    implementationDatastore()
    implementationCoroutines()
    implementationHilt()
}
