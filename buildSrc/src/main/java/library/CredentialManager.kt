import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object CredentialManager {
    private const val CREDENTIALS_VERSION = "1.3.0"
    private const val GOOGLE_ID_VERSION = "1.1.1"

    internal const val CREDENTIALS_PATH = "androidx.credentials:credentials:$CREDENTIALS_VERSION"
    internal const val CREDENTIALS_PLAY_SERVICES_PATH =
        "androidx.credentials:credentials-play-services-auth:$CREDENTIALS_VERSION"
    internal const val GOOGLE_ID_PATH =
        "com.google.android.libraries.identity.googleid:googleid:$GOOGLE_ID_VERSION"
}

fun DependencyHandlerScope.implementationCredentialManager() {
    implementation(CredentialManager.CREDENTIALS_PATH)
    implementation(CredentialManager.CREDENTIALS_PLAY_SERVICES_PATH)
    implementation(CredentialManager.GOOGLE_ID_PATH)
}
