object SoLoader {
    private const val SO_LOADER_VERSION = "0.10.5"

    // Fresco (через cloudinary-android) тягне 0.10.1, який Play Console позначає як
    // причину падінь на 64-бітних пристроях. Піднімаємо через constraint у :app.
    const val SO_LOADER_PATH = "com.facebook.soloader:soloader:$SO_LOADER_VERSION"
}
