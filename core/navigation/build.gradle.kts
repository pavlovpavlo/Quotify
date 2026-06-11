plugins {
    AndroidLibrary
    KotlinAndroid
    Compose
    Hilt
    Ktlint
    KotlinKapt
    KotlinSerialization
}

android {
    namespace = "${AppConfig.applicationId}.core.navigation"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementationCompose()
    apiNavigation3()
    implementationSerialization()
    implementationHilt()
    implementationCoroutines()
}
