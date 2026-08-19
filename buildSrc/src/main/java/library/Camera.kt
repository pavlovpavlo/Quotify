import extension.compileOnly
import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Camera {
    private const val CAMERAX_VERSION = "1.4.0"
    private const val GUAVA_VERSION = "31.1-android"

    internal const val CAMERA_CORE_PATH = "androidx.camera:camera-core:$CAMERAX_VERSION"
    internal const val CAMERA_CAMERA2_PATH = "androidx.camera:camera-camera2:$CAMERAX_VERSION"
    internal const val CAMERA_LIFECYCLE_PATH = "androidx.camera:camera-lifecycle:$CAMERAX_VERSION"
    internal const val CAMERA_VIEW_PATH = "androidx.camera:camera-view:$CAMERAX_VERSION"

    // Firebase Analytics підтягує повну guava у runtime, через що CameraX-стаб
    // listenablefuture зникає з compile classpath — ListenableFuture беремо звідси.
    internal const val GUAVA_PATH = "com.google.guava:guava:$GUAVA_VERSION"
}

fun DependencyHandlerScope.implementationCamera() {
    implementation(Camera.CAMERA_CORE_PATH)
    implementation(Camera.CAMERA_CAMERA2_PATH)
    implementation(Camera.CAMERA_LIFECYCLE_PATH)
    implementation(Camera.CAMERA_VIEW_PATH)
    compileOnly(Camera.GUAVA_PATH)
}
