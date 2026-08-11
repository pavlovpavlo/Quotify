package com.kovhan.core.network

/**
 * Помилка виклику бекенду. `code` — машинний код із воркера
 * (`unauthenticated`, `invalid_argument`, `token_owned_by_another_account`, ...),
 * або [NETWORK_UNAVAILABLE] / [UNEXPECTED], коли відповіді не було взагалі.
 */
data class ApiError(
    val code: String,
    val message: String? = null,
    val httpStatus: Int? = null,
) {
    companion object {
        const val NETWORK_UNAVAILABLE = "network_unavailable"
        const val UNEXPECTED = "unexpected"
    }
}
