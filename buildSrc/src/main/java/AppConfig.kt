object AppConfig {
    const val applicationId = "com.kovhan"

    const val compileSdk = 36
    const val minSdk = 28
    const val targetSdk = 35

    const val proguardConsumerRules =  "consumer-rules.pro"
    const val dimension = "environment"
    const val targetJvm = "21"

    object BuildVariants {
        const val RELEASE = "release"
        const val DEBUG = "debug"
    }

    object AppVersion {
        private const val MAJOR = 1
        private const val MINOR = 0 // Maximum value 99
        private const val PATCH = 1 // Maximum value 99

        fun getName(): String {
            return "$MAJOR.$MINOR.$PATCH"
        }

        fun getCode(): Int {
            return MAJOR * 10000 + MINOR * 100 + PATCH
        }
    }
}