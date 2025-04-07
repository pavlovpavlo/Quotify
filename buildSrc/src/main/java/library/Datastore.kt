import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Datastore {
    internal const val DATASTORE_VERSION = "1.0.0"

    internal const val DATASTORE_PATH = "androidx.datastore:datastore-preferences:$DATASTORE_VERSION"
}

fun DependencyHandlerScope.implementationDatastore() {
    implementation(Datastore.DATASTORE_PATH)
}