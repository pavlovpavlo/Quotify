import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Logs {
    internal const val TIMBER_VERSION = "5.0.1"

    internal const val TIMBER_PATH = "com.jakewharton.timber:timber:$TIMBER_VERSION"
}

fun DependencyHandlerScope.implementationLogs() {
    implementation(Logs.TIMBER_PATH)
}
