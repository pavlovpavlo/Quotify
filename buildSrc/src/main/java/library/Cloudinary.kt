import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Cloudinary {
    private const val CLOUDINARY_VERSION = "2.5.0"

    internal const val CLOUDINARY_ANDROID_PATH = "com.cloudinary:cloudinary-android:$CLOUDINARY_VERSION"
}

/**
 * Fresco приїжджає транзитивно через `cloudinary-android-ui` і кладе в APK три
 * нативні бібліотеки з вирівнюванням 4 KB, через які застосунок не відповідає
 * вимозі 16 KB page size. UI-частина Cloudinary тут не використовується —
 * потрібен лише `MediaManager` для завантаження фото профілю.
 */
fun DependencyHandlerScope.implementationCloudinary() {
    implementation(Cloudinary.CLOUDINARY_ANDROID_PATH) {
        exclude(mapOf("group" to "com.facebook.fresco"))
    }
}
