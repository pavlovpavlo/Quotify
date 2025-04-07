import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    AndroidLibrary
    KotlinAndroid
    Compose
    Ktlint
}

android {
    namespace = "${AppConfig.applicationId}.design.systems"
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

    implementationAndroidX()
    implementationCompose()
    implementationAccompanist()
    implementationLogs()

    implementationAndroidTests()
    implementationTests()
}