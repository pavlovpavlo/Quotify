package com.kovhan.core.models

/** Failure reasons for AI-backed features (OCR, tag suggestions). */
sealed interface AiError {
    /** The device has no internet connection. */
    data object Offline : AiError

    /** Any other failure (model error, decode failure, unexpected response). */
    data object Unknown : AiError
}
