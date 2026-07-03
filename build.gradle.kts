import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.plugins.LibraryPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    AndroidApplication version PLUGIN_ANDROID_VERSION apply false
    AndroidLibrary version PLUGIN_ANDROID_VERSION apply false
    Crashlytics version PLUGIN_CRASHLYTICS_VERSION apply false
    GoogleServices version PLUGIN_GOOGLE_SERVICES_VERSION apply false
    Hilt version PLUGIN_HITL_VERSION apply false
    KotlinAndroid version PLUGIN_KOTLIN_VERSION apply false
    KotlinKapt version PLUGIN_KOTLIN_VERSION apply false
    Compose version PLUGIN_KOTLIN_VERSION apply false
    KotlinSerialization version PLUGIN_KOTLIN_VERSION apply false
    Ktlint version PLUGIN_KTLINT_VERSION apply false
    Room version PLUGIN_ROOM_VERSION apply false
}

fun BaseExtension.baseConfig() {
    compileSdkVersion(AppConfig.compileSdk)

    defaultConfig.apply {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = AppConfig.targetJvm
        }
    }

    testOptions {
        unitTests.all { test ->
            test.useJUnitPlatform()
        }
    }

}

/**
 * Apply configuration settings that are shared across all modules.
 */
fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is AppPlugin -> {
                project.extensions
                    .getByType<AppExtension>()
                    .apply {
                        baseConfig()
                    }
            }

            is LibraryPlugin -> {
                project.extensions
                    .getByType<LibraryExtension>()
                    .apply {
                        baseConfig()
                        buildFeatures {
                            buildConfig = true
                        }
                    }
            }
        }
    }
}

subprojects {
    project.plugins.applyBaseConfig(project)
}