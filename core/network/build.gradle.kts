plugins {
    AndroidLibrary
    KotlinAndroid
    KotlinKapt
    KotlinSerialization
    Hilt
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.core.network"

    defaultConfig {
        // Базовий URL Cloudflare Worker. Після `wrangler deploy` підставити свій піддомен.
        buildConfigField(
            "String",
            "BILLING_API_BASE_URL",
            "\"https://quotify-billing.pavlovpavlo2013.workers.dev\"",
        )
    }
}

dependencies {
    api(project(":core:models"))
    implementationNetworking()
    implementationSerialization()
    implementationCoroutines()
    implementationHilt()
    implementationLogs()
}
