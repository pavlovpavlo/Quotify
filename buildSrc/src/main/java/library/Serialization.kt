import extension.api
import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

private object Serialization {

    object Version {
        const val KOTLIN_SERIALIZATION = "1.7.3"
    }

    const val KOTLIN_SERIALIZATION_PATH = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.KOTLIN_SERIALIZATION}"
}

fun DependencyHandlerScope.implementationSerialization() {
    implementation(Serialization.KOTLIN_SERIALIZATION_PATH)
}

fun DependencyHandlerScope.apiSerialization() {
    api(Serialization.KOTLIN_SERIALIZATION_PATH)
}