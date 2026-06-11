import extension.api
import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Navigation3 {

    internal const val NAVIGATION3_VERSION = "1.1.2"
    internal const val LIFECYCLE_NAV3_VERSION = "2.10.0"
    internal const val NAVIGATION_EVENT_VERSION = "1.0.2"

    internal const val RUNTIME_PATH = "androidx.navigation3:navigation3-runtime:$NAVIGATION3_VERSION"
    internal const val UI_PATH = "androidx.navigation3:navigation3-ui:$NAVIGATION3_VERSION"
    internal const val LIFECYCLE_VIEWMODEL_PATH =
        "androidx.lifecycle:lifecycle-viewmodel-navigation3:$LIFECYCLE_NAV3_VERSION"
    internal const val NAVIGATION_EVENT_COMPOSE_PATH =
        "androidx.navigationevent:navigationevent-compose:$NAVIGATION_EVENT_VERSION"
}

fun DependencyHandlerScope.implementationNavigation3() {
    implementation(Navigation3.RUNTIME_PATH)
    implementation(Navigation3.UI_PATH)
    implementation(Navigation3.LIFECYCLE_VIEWMODEL_PATH)
    implementation(Navigation3.NAVIGATION_EVENT_COMPOSE_PATH)
}

fun DependencyHandlerScope.apiNavigation3() {
    api(Navigation3.RUNTIME_PATH)
    api(Navigation3.UI_PATH)
    api(Navigation3.LIFECYCLE_VIEWMODEL_PATH)
    api(Navigation3.NAVIGATION_EVENT_COMPOSE_PATH)
}
