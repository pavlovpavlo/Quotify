import extension.implementation
import extension.implementationPlatform
import org.gradle.kotlin.dsl.DependencyHandlerScope

object FirebaseDependencies {

    private const val FIREBASE_VERSION = "32.7.1"

    internal const val FIREBASE_BOM_PATH = "com.google.firebase:firebase-bom:$FIREBASE_VERSION"
    internal const val FIREBASE_AUTH = "com.google.firebase:firebase-auth"
    internal const val FIREBASE_ANALYTICS_PATH = "com.google.firebase:firebase-analytics-ktx"
    internal const val FIREBASE_CRASHLYTICS_PATH = "com.google.firebase:firebase-crashlytics-ktx"
    internal const val FIREBASE_MESSAGING_PATH = "com.google.firebase:firebase-messaging-ktx"
    internal const val FIREBASE_DYNAMIC_LINKS = "com.google.firebase:firebase-dynamic-links-ktx"
    internal const val FIREBASE_FIRESTORE_PATH = "com.google.firebase:firebase-firestore-ktx"

}

fun DependencyHandlerScope.implementationFirebase() {
    implementationPlatform(FirebaseDependencies.FIREBASE_BOM_PATH)
    implementation(FirebaseDependencies.FIREBASE_AUTH)
    implementation(FirebaseDependencies.FIREBASE_ANALYTICS_PATH)
    implementation(FirebaseDependencies.FIREBASE_CRASHLYTICS_PATH)
    implementation(FirebaseDependencies.FIREBASE_MESSAGING_PATH)
    implementation(FirebaseDependencies.FIREBASE_DYNAMIC_LINKS)
    implementation(FirebaseDependencies.FIREBASE_FIRESTORE_PATH)
}
