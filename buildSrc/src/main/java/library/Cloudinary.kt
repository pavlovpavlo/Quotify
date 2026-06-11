import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Cloudinary {
    private const val CLOUDINARY_VERSION = "2.5.0"

    internal const val CLOUDINARY_ANDROID_PATH = "com.cloudinary:cloudinary-android:$CLOUDINARY_VERSION"
}

fun DependencyHandlerScope.implementationCloudinary() {
    implementation(Cloudinary.CLOUDINARY_ANDROID_PATH)
}
