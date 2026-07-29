import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

private object GlanceDependencies {
    const val GLANCE_VERSION = "1.1.1"
    const val WORK_VERSION = "2.9.1"

    const val GLANCE_APPWIDGET = "androidx.glance:glance-appwidget:$GLANCE_VERSION"
    const val WORK_RUNTIME = "androidx.work:work-runtime-ktx:$WORK_VERSION"
}

fun DependencyHandlerScope.implementationGlance() {
    implementation(GlanceDependencies.GLANCE_APPWIDGET)
    implementation(GlanceDependencies.WORK_RUNTIME)
}
