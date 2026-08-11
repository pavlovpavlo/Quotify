import extension.implementation
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Billing {
    private const val BILLING_VERSION = "9.1.0"

    // Навмисно без -ktx: той артефакт зібраний Kotlin 2.3, а проєкт на 2.1, тож його
    // метадані не читаються. Suspend-обгортки над листенерами пишемо у BillingService.
    internal const val BILLING_ANDROID_PATH = "com.android.billingclient:billing:$BILLING_VERSION"
}

fun DependencyHandlerScope.implementationBilling() {
    implementation(Billing.BILLING_ANDROID_PATH)
}
