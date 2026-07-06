plugins {
    AndroidApplication
    Crashlytics
    GoogleServices
    Hilt
    KotlinAndroid
    Compose
    KotlinSerialization
    Ktlint
    KotlinKapt
}

android {
    namespace = "${AppConfig.applicationId}.quotify"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.kovhan.quotify"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":domain"))
    implementation(project(":core:firebase"))
    implementation(project(":data:auth"))
    implementation(project(":data:settings"))
    implementation(project(":data:library"))
    implementation(project(":design-systems"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:main"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:webview"))
    implementationAndroidX()
    implementationCompose()
    implementationHilt()
    implementationFirebase()
    implementationCoroutines()
    implementationNavigation()
    implementationSerialization()
    implementationLogs()
    implementationTests()
}