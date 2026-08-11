package com.kovhan.core.network.api

import com.kovhan.core.models.Outcome
import com.kovhan.core.network.ApiError
import com.kovhan.core.network.BuildConfig
import com.kovhan.core.network.api.dto.ApiErrorResponse
import com.kovhan.core.network.api.dto.VerifyPurchaseRequest
import com.kovhan.core.network.api.dto.VerifyPurchaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BillingApi @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun verifyPurchase(
        idToken: String,
        productId: String,
        purchaseToken: String,
    ): Outcome<VerifyPurchaseResponse, ApiError> = runCatching {
        val response: HttpResponse = client.post("$BASE_URL$PATH_VERIFY") {
            bearerAuth(idToken)
            setBody(VerifyPurchaseRequest(productId = productId, purchaseToken = purchaseToken))
        }
        if (response.status.isSuccess()) {
            Outcome.Success(response.body<VerifyPurchaseResponse>())
        } else {
            Outcome.Failure(response.toApiError())
        }
    }.getOrElse { throwable ->
        Outcome.Failure(
            when (throwable) {
                is IOException -> ApiError(ApiError.NETWORK_UNAVAILABLE, throwable.message)
                else -> ApiError(ApiError.UNEXPECTED, throwable.message)
            },
        )
    }

    private suspend fun HttpResponse.toApiError(): ApiError {
        val parsed = runCatching { body<ApiErrorResponse>() }.getOrNull()
        return ApiError(
            code = parsed?.error ?: ApiError.UNEXPECTED,
            message = parsed?.message,
            httpStatus = status.value,
        )
    }

    private companion object {
        const val BASE_URL = BuildConfig.BILLING_API_BASE_URL
        const val PATH_VERIFY = "/verify"
    }
}
