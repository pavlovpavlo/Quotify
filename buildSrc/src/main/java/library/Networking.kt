import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Networking {
    internal const val NETWORKING_VERSION = "2.3.8"
    internal const val SOCKET_IO_VERSION = "2.1.0"
    internal const val JWT_DECODE_VERSION = "2.0.2"

    internal const val KTOR_CLIENT_CORE_PATH = "io.ktor:ktor-client-core:$NETWORKING_VERSION"
    internal const val KTOR_AUTH_PATH = "io.ktor:ktor-client-auth:$NETWORKING_VERSION"
    internal const val KTOR_CLIENT_CNDROID_PATH = "io.ktor:ktor-client-android:$NETWORKING_VERSION"
    internal const val KTOR_CLIENT_SERIALIZATION_PATH = "io.ktor:ktor-client-serialization:$NETWORKING_VERSION"
    internal const val KTOR_CLIENT_LOGGING_PATH = "io.ktor:ktor-client-logging-jvm:$NETWORKING_VERSION"
    internal const val KTOR_CLIENT_CONTENT_NEGOTIATION_PATH = "io.ktor:ktor-client-content-negotiation:$NETWORKING_VERSION"
    internal const val KTOR_CLIENT_KOTLIN_SERIALIZATION_PATH = "io.ktor:ktor-serialization-kotlinx-json:$NETWORKING_VERSION"
    internal const val SOCKET_IO = "io.socket:socket.io-client:$SOCKET_IO_VERSION"
    internal const val JWT_DECODE = "com.auth0.android:jwtdecode:$JWT_DECODE_VERSION"
}

fun DependencyHandlerScope.implementationNetworking() {
    implementation(Networking.KTOR_CLIENT_CORE_PATH)
    implementation(Networking.KTOR_AUTH_PATH)
    implementation(Networking.KTOR_CLIENT_CNDROID_PATH)
    implementation(Networking.KTOR_CLIENT_SERIALIZATION_PATH)
    implementation(Networking.KTOR_CLIENT_LOGGING_PATH)
    implementation(Networking.KTOR_CLIENT_CONTENT_NEGOTIATION_PATH)
    implementation(Networking.KTOR_CLIENT_KOTLIN_SERIALIZATION_PATH)
    implementation(Networking.SOCKET_IO)
    implementation(Networking.JWT_DECODE)
}