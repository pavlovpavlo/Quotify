import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Review {
    private const val REVIEW_VERSION = "2.0.2"

    internal const val REVIEW_PATH = "com.google.android.play:review:$REVIEW_VERSION"
}

fun DependencyHandlerScope.implementationReview() {
    implementation(Review.REVIEW_PATH)
}
