import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Gson {
    internal const val GSON_VERSION = "2.10.1"

    internal const val GSON_PATH = "com.google.code.gson:gson:$GSON_VERSION"
}

fun DependencyHandlerScope.implementationGson() {
    implementation(Gson.GSON_PATH)
}
