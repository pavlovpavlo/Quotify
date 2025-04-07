import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Navigation {
    internal const val NAVIGATION_VERSION = "2.8.5"

    internal const val NAVIGATION_PATH = "androidx.navigation:navigation-compose:$NAVIGATION_VERSION"
}

fun DependencyHandlerScope.implementationNavigation() {
    implementation(Navigation.NAVIGATION_PATH)
}
