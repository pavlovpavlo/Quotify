import extension.implementation
import extension.kapt
import org.gradle.kotlin.dsl.DependencyHandlerScope

object HiltDependencies {

    internal const val HILT_VERSION = "2.54"
    internal const val HILT_NAVIGATION_COMPOSE_VERSION = "1.2.0"

    internal const val HILT_ANDROID_PATH = "com.google.dagger:hilt-android:$HILT_VERSION"
    internal const val HILT_NAVIGATION_COMPOSE_PATH = "androidx.hilt:hilt-navigation-compose:$HILT_NAVIGATION_COMPOSE_VERSION"
    internal const val HILT_ANDROID_COMPILER_PATH = "com.google.dagger:hilt-android-compiler:$HILT_VERSION"

}

fun DependencyHandlerScope.implementationHilt() {
    implementation(HiltDependencies.HILT_ANDROID_PATH)
    implementation(HiltDependencies.HILT_NAVIGATION_COMPOSE_PATH)
    kapt(HiltDependencies.HILT_ANDROID_COMPILER_PATH)
}