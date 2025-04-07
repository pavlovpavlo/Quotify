import extension.debugImplementation
import extension.implementation
import extension.implementationPlatform
import org.gradle.kotlin.dsl.DependencyHandlerScope

private object ComposeDependencies {

    object Version {
        const val BOM = "2024.12.01"
        const val ACTIVITY = "1.9.3"
        const val LIFECYCLE = "2.8.7"
        const val CONSTRAINT_LAYOUT = "1.1.0"
        const val LOTTY = "6.6.2"
        const val COIL = "2.7.0"
    }

    const val COMPOSE_BOM_PATH = "androidx.compose:compose-bom:${Version.BOM}"
    const val COMPOSE_UI_PATH = "androidx.compose.ui:ui:"
    const val COMPOSE_UI_GRAPHICS_PATH = "androidx.compose.ui:ui-graphics"
    const val COMPOSE_UI_TOOLING_PATH = "androidx.compose.ui:ui-tooling"
    const val COMPOSE_UI_TOOLING_PREVIEW_PATH = "androidx.compose.ui:ui-tooling-preview"
    const val COMPOSE_UI_UTIL = "androidx.compose.ui:ui-util"
    const val COMPOSE_ANIMATIONS_PATH = "androidx.compose.animation:animation"
    const val COMPOSE_FOUNDATION_PATH = "androidx.compose.foundation:foundation"
    const val COMPOSE_MATERIAL3_PATH = "androidx.compose.material3:material3"
    const val COMPOSE_MATERIAL3_WINDOW_SIZE_PATH = "androidx.compose.material3:material3-window-size-class"
    const val COMPOSE_RUNTIME_PATH = "androidx.compose.runtime:runtime"

    const val COMPOSE_ACTIVITY_PATH = "androidx.activity:activity-compose:${Version.ACTIVITY}"
    const val COMPOSE_LIFECYCLE_RUNTIME_PATH = "androidx.lifecycle:lifecycle-runtime-compose:${Version.LIFECYCLE}"
    const val COMPOSE_VIEWMODEL_PATH = "androidx.lifecycle:lifecycle-viewmodel-compose:${Version.LIFECYCLE}"
    const val COMPOSE_LOTTIE_PATH = "com.airbnb.android:lottie-compose:${Version.LOTTY}"
    const val COMPOSE_CONSTRAINT_PATH = "androidx.constraintlayout:constraintlayout-compose:${Version.CONSTRAINT_LAYOUT}"
    const val COMPOSE_COIL_PATH = "io.coil-kt:coil-compose:${Version.COIL}"
    const val COMPOSE_COIL_SVG_PATH = "io.coil-kt:coil-svg:${Version.COIL}"

}

fun DependencyHandlerScope.implementationCompose() {
    implementationPlatform(ComposeDependencies.COMPOSE_BOM_PATH)
    implementation(ComposeDependencies.COMPOSE_UI_PATH)
    implementation(ComposeDependencies.COMPOSE_UI_GRAPHICS_PATH)
    implementation(ComposeDependencies.COMPOSE_UI_TOOLING_PREVIEW_PATH)
    implementation(ComposeDependencies.COMPOSE_UI_UTIL)
    implementation(ComposeDependencies.COMPOSE_ANIMATIONS_PATH)
    implementation(ComposeDependencies.COMPOSE_FOUNDATION_PATH)
    implementation(ComposeDependencies.COMPOSE_MATERIAL3_PATH)
    implementation(ComposeDependencies.COMPOSE_MATERIAL3_WINDOW_SIZE_PATH)
    implementation(ComposeDependencies.COMPOSE_RUNTIME_PATH)
    implementation(ComposeDependencies.COMPOSE_ACTIVITY_PATH)
    implementation(ComposeDependencies.COMPOSE_LIFECYCLE_RUNTIME_PATH)
    implementation(ComposeDependencies.COMPOSE_VIEWMODEL_PATH)
    implementation(ComposeDependencies.COMPOSE_LOTTIE_PATH)
    implementation(ComposeDependencies.COMPOSE_CONSTRAINT_PATH)
    implementation(ComposeDependencies.COMPOSE_COIL_PATH)
    implementation(ComposeDependencies.COMPOSE_COIL_SVG_PATH)
    debugImplementation(ComposeDependencies.COMPOSE_UI_TOOLING_PATH)
}