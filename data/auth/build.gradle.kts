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
    implementationDatabase()
    implementationDatastore()
    implementationFirebase()
    implementationLogs()
    implementationNetworking()
    implementationHilt()
    implementationGson()
    implementationTests()
}
