import extension.implementation
import extension.kapt
import extension.ksp
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Database {
    internal const val ROOM_VERSION = "2.7.0-alpha12"

    internal const val ROOM_COMPILER_PATH = "androidx.room:room-compiler:$ROOM_VERSION"
    internal const val ROOM_DATABASE_PATH = "androidx.room:room-runtime:$ROOM_VERSION"
    internal const val ROOM_KTX_PATH = "androidx.room:room-ktx:$ROOM_VERSION"
}

fun DependencyHandlerScope.implementationDatabase() {
    kapt(Database.ROOM_COMPILER_PATH)
    implementation(Database.ROOM_DATABASE_PATH)
    implementation(Database.ROOM_KTX_PATH)
}
