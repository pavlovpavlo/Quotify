package com.kovhan.core.models.billing

enum class BillingError {
    NETWORK,
    NOT_AUTHENTICATED,
    TOKEN_OWNED_BY_ANOTHER_ACCOUNT,
    UNKNOWN_PURCHASE,
    BACKEND,
    UNKNOWN,
}
