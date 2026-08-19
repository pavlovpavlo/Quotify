import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    AndroidLibrary
    KotlinAndroid
    Compose
    KotlinKapt
    Hilt
    Ktlint
    KotlinSerialization
}

android {
    namespace = "${AppConfig.applicationId}.feature.onboarding"
    buildFeatures {
        compose = true
    }

    composeCompiler {
        featureFlags.addAll(
            ComposeFeatureFlag.StrongSkipping,
            ComposeFeatureFlag.OptimizeNonSkippingGroups
        )
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:analytics"))
    implementation(project(":core:navigation"))
    implementation(project(":design-systems"))
    implementation(project(":domain"))
    implementationAndroidX()
    implementationCompose()
    implementationCoroutines()
    implementationDatastore()
    implementationNavigation()
    implementationSerialization()
    implementationLogs()
    implementationHilt()
    implementationTests()
}