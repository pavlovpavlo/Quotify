import extension.androidTestImplementation
import extension.testImplementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Test {
    private const val JUNIT5_VERSION = "5.11.4"
    private const val MOCKK_VERSION = "1.13.14"
    private const val COROUTINES_TEST_VERSION = "1.8.0"
    private const val TURBINE_VERSION = "1.2.0"

    private const val ANDROID_JUNIT_VERSION = "1.2.1"
    private const val ESPRESSO_VERSION = "3.6.1"

    internal const val JUNIT5_PATH = "org.junit.jupiter:junit-jupiter:$JUNIT5_VERSION"
    internal const val MOCKK_PATH = "io.mockk:mockk:$MOCKK_VERSION"
    internal const val COROUTINES_TEST_PATH =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_TEST_VERSION"
    internal const val TURBINE_PATH = "app.cash.turbine:turbine:$TURBINE_VERSION"

    internal const val ANDROID_JUNIT_PATH = "androidx.test.ext:junit:$ANDROID_JUNIT_VERSION"
    internal const val ESPRESSO_PATH = "androidx.test.espresso:espresso-core:$ESPRESSO_VERSION"
}

fun DependencyHandlerScope.implementationTests() {
    testImplementation(Test.JUNIT5_PATH)
    testImplementation(Test.MOCKK_PATH)
    testImplementation(Test.COROUTINES_TEST_PATH)
    testImplementation(Test.TURBINE_PATH)
}

fun DependencyHandlerScope.implementationAndroidTests() {
    androidTestImplementation(Test.ANDROID_JUNIT_PATH)
    androidTestImplementation(Test.ESPRESSO_PATH)
}