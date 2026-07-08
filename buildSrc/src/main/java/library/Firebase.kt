import extension.debugImplementation
import extension.implementation
import extension.implementationPlatform
import org.gradle.kotlin.dsl.DependencyHandlerScope

object FirebaseDependencies {

    private const val FIREBASE_VERSION = "33.16.0"

    internal const val FIREBASE_BOM_PATH = "com.google.firebase:firebase-bom:$FIREBASE_VERSION"
    internal const val FIREBASE_AUTH = "com.google.firebase:firebase-auth"
    internal const val FIREBASE_ANALYTICS_PATH = "com.google.firebase:firebase-analytics"
    internal const val FIREBASE_CRASHLYTICS_PATH = "com.google.firebase:firebase-crashlytics"
    internal const val FIREBASE_MESSAGING_PATH = "com.google.firebase:firebase-messaging"
    internal const val FIREBASE_FIRESTORE_PATH = "com.google.firebase:firebase-firestore"
    internal const val FIREBASE_AI_PATH = "com.google.firebase:firebase-ai"
    internal const val FIREBASE_APPCHECK_PLAY_INTEGRITY = "com.google.firebase:firebase-appcheck-playintegrity"
    internal const val FIREBASE_APPCHECK_DEBUG = "com.google.firebase:firebase-appcheck-debug"

}

fun DependencyHandlerScope.implementationFirebase() {
    implementationPlatform(FirebaseDependencies.FIREBASE_BOM_PATH)
    implementation(FirebaseDependencies.FIREBASE_AUTH)
    implementation(FirebaseDependencies.FIREBASE_ANALYTICS_PATH)
    implementation(FirebaseDependencies.FIREBASE_CRASHLYTICS_PATH)
    implementation(FirebaseDependencies.FIREBASE_MESSAGING_PATH)
    implementation(FirebaseDependencies.FIREBASE_FIRESTORE_PATH)
    implementation(FirebaseDependencies.FIREBASE_APPCHECK_PLAY_INTEGRITY)
    debugImplementation(FirebaseDependencies.FIREBASE_APPCHECK_DEBUG)
}

fun DependencyHandlerScope.implementationFirebaseAi() {
    implementationPlatform(FirebaseDependencies.FIREBASE_BOM_PATH)
    implementation(FirebaseDependencies.FIREBASE_AI_PATH)
}
