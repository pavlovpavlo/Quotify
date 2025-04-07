import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object AndroidX {
    private const val ANDROID_CORE_VERSION = "1.12.0"
    private const val ANDROID_ANIMATE_CORE_VERSION = "1.0.1"
    private const val ANDROID_APPCOMPAT_VERSION = "1.6.1"
    private const val ANDROID_ACTIVITY_VERSION = "1.8.2"
    private const val ANDROID_WINDOW_VERSION = "1.2.0"

    internal const val ANDROID_CORE_PATH = "androidx.core:core-ktx:$ANDROID_CORE_VERSION"
    internal const val ANDROID_CORE_SPLASH_PATH = "androidx.core:core-splashscreen:$ANDROID_ANIMATE_CORE_VERSION"
    internal const val ANDROID_APPCOMPAT_PATH = "androidx.appcompat:appcompat:$ANDROID_APPCOMPAT_VERSION"
    internal const val ANDROID_ACTIVITY_PATH = "androidx.activity:activity-ktx:${ANDROID_ACTIVITY_VERSION}"
    internal const val ANDROID_WINDOW_PATH = "androidx.window:window:$ANDROID_WINDOW_VERSION"
}

fun DependencyHandlerScope.implementationAndroidX() {
    implementation(AndroidX.ANDROID_CORE_SPLASH_PATH)
    implementation(AndroidX.ANDROID_CORE_PATH)
    implementation(AndroidX.ANDROID_APPCOMPAT_PATH)
    implementation(AndroidX.ANDROID_ACTIVITY_PATH)
    implementation(AndroidX.ANDROID_WINDOW_PATH)
}