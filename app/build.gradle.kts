import java.util.Properties

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

val keystoreProperties = Properties().apply {
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use(::load)
    }
}
val hasReleaseSigning = keystoreProperties.getProperty("storeFile") != null

android {
    namespace = "${AppConfig.applicationId}.quotify"
    compileSdk = AppConfig.compileSdk

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    defaultConfig {
        applicationId = "com.kovhan.quotify"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = (project.findProperty("versionCode") as String?)?.toInt()
            ?: AppConfig.AppVersion.getCode()
        versionName = (project.findProperty("versionName") as String?)
            ?: AppConfig.AppVersion.getName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = if (hasReleaseSigning) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
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
    constraints {
        implementation(SoLoader.SO_LOADER_PATH) {
            because("Play Console: SoLoader < 0.10.4 crashes on 64-bit devices")
        }
    }

    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))
    implementation(project(":domain"))
    implementation(project(":core:firebase"))
    implementation(project(":core:network"))
    implementation(project(":core:billing"))
    implementation(project(":data:auth"))
    implementation(project(":data:settings"))
    implementation(project(":data:library"))
    implementation(project(":data:billing"))
    implementation(project(":data:voice"))
    implementation(project(":data:scan"))
    implementation(project(":data:feedback"))
    implementation(project(":design-systems"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:main"))
    implementation(project(":feature:entitydetails"))
    implementation(project(":feature:search"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:webview"))
    implementation(project(":feature:addquote"))
    implementation(project(":feature:widget"))
    implementation(project(":feature:subscription"))
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
