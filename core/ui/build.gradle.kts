import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

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

    namespace = "${AppConfig.applicationId}.core.ui"

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeCompiler {
        featureFlags.addAll(
            ComposeFeatureFlag.StrongSkipping,
            ComposeFeatureFlag.OptimizeNonSkippingGroups,
            ComposeFeatureFlag.PausableComposition
        )
    }

}

dependencies {
    apiSerialization()
    implementationAndroidX()
    implementationCompose()
    implementationCoroutines()
    implementationNavigation()
    implementationLogs()
    implementationGson()
    implementationHilt()
    implementationTests()
}