import extension.androidTestImplementation
import extension.testImplementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Test {
    internal const val JUNIT_VERSION = "4.13.2"
    internal const val ANDROID_JUNIT_VERSION = "1.2.1"
    internal const val ESPRESSO_VERSION = "3.6.1"
    internal const val MOCKITO_VERSION = "5.15.2"
    internal const val MOCKITO_KOTLIN_VERSION = "5.4.0"

    internal const val JUNIT_PATH = "junit:junit:$JUNIT_VERSION"
    internal const val ANDROID_JUNIT_PATH = "androidx.test.ext:junit:$ANDROID_JUNIT_VERSION"
    internal const val ESPRESSO_PATH = "androidx.test.espresso:espresso-core:$ESPRESSO_VERSION"
    internal const val MOCKITO_CORE_PATH = "org.mockito:mockito-core:$MOCKITO_VERSION"
    internal const val MOCKITO_KOTLIN_PATH = "org.mockito.kotlin:mockito-kotlin:$MOCKITO_KOTLIN_VERSION"

}

fun DependencyHandlerScope.implementationTests() {
    testImplementation(Test.JUNIT_PATH)
    testImplementation(Test.MOCKITO_CORE_PATH)
    testImplementation(Test.MOCKITO_KOTLIN_PATH)
}

fun DependencyHandlerScope.implementationAndroidTests() {
    androidTestImplementation(Test.ANDROID_JUNIT_PATH)
    androidTestImplementation(Test.ESPRESSO_PATH)
}
