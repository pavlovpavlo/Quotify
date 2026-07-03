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
    implementation(project(":core:datastore"))
    implementationDatabase()
    implementationDatastore()
    implementationFirebase()
    implementationCloudinary()
    implementationCoroutines()
    implementationLogs()
    implementationNetworking()
    implementationHilt()
    implementationGson()
    implementationTests()
}
