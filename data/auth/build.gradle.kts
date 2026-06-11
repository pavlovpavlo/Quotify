plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinSerialization
    KotlinKapt
    Hilt
    Ktlint
    Room
}

android {
    namespace = "${AppConfig.applicationId}.data.auth"

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":domain"))
    implementationDatabase()
    implementationDatastore()
    implementationFirebase()
    implementationCloudinary()
    implementationCredentialManager()
    implementationCoroutines()
    implementationLogs()
    implementationNetworking()
    implementationHilt()
    implementationGson()
    implementationTests()
}
