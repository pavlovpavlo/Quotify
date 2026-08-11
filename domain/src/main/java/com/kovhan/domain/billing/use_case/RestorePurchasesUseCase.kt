package com.kovhan.domain.billing.use_case

import com.kovhan.domain.billing.BillingRepository
import javax.inject.Inject

/**
 * Кнопка «Відновити покупки»: Play віддає активні підписки цього Google-акаунта,
 * кожна з них іде на повторну верифікацію, статус у Firestore оновлюється.
 * Результат прилітає в [ObservePurchaseVerificationsUseCase].
 */
class RestorePurchasesUseCase @Inject constructor(
    private val repository: BillingRepository,
) {
    suspend operator fun invoke() {
        if (repository.connect()) repository.refreshPurchases()
    }
}
