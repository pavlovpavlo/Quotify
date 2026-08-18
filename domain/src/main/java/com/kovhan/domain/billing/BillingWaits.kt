package com.kovhan.domain.billing

object BillingWaits {

    const val MAX_VERIFY_ATTEMPTS = 3

    const val VERIFY_RETRY_STEP_MS = 1_500L

    private const val RETRY_SLEEP_MS =
        VERIFY_RETRY_STEP_MS * (MAX_VERIFY_ATTEMPTS - 1) * MAX_VERIFY_ATTEMPTS / 2

    private const val ROUND_TRIP_ALLOWANCE_MS = 2_500L * MAX_VERIFY_ATTEMPTS

    const val VERIFICATION_GRACE_MS = RETRY_SLEEP_MS + ROUND_TRIP_ALLOWANCE_MS
}
