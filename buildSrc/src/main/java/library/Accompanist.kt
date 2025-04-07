import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Accompanist {

    private const val ACCOMPANIST_CORE_VERSION = "0.32.0"

    internal const val ACCOMPANIST_FLOW_LAYOUT_PATH =
        "com.google.accompanist:accompanist-flowlayout:$ACCOMPANIST_CORE_VERSION"
    internal const val ACCOMPANIST_NAVIGATION_ANIMATION_PATH =
        "com.google.accompanist:accompanist-navigation-animation:$ACCOMPANIST_CORE_VERSION"
    internal const val ACCOMPANIST_SYSTEM_UI_CONTOLLER_PATH =
        "com.google.accompanist:accompanist-systemuicontroller:$ACCOMPANIST_CORE_VERSION"
    internal const val ACCOMPANIST_PERMISSIONS_PATH =
        "com.google.accompanist:accompanist-permissions:$ACCOMPANIST_CORE_VERSION"
}

fun DependencyHandlerScope.implementationAccompanist() {
    implementation(Accompanist.ACCOMPANIST_FLOW_LAYOUT_PATH)
    implementation(Accompanist.ACCOMPANIST_NAVIGATION_ANIMATION_PATH)
    implementation(Accompanist.ACCOMPANIST_SYSTEM_UI_CONTOLLER_PATH)
    implementation(Accompanist.ACCOMPANIST_PERMISSIONS_PATH)
}
