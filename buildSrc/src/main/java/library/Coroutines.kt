import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Coroutines {
    private const val COROUTINES_VERSION = "1.8.0"

    internal const val COROUTINES_PATH = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION"
}

fun DependencyHandlerScope.implementationCoroutines() {
    implementation(Coroutines.COROUTINES_PATH)
}
