package com.kovhan.domain.scan

import android.net.Uri
import com.kovhan.core.models.AiError
import com.kovhan.core.models.Outcome
import com.kovhan.domain.scan.model.RecognizedTextLine

/**
 * Abstraction over Gemini-backed optical character recognition.
 *
 * [recognize] decodes the image behind [image] and returns the text it finds,
 * split into lines. A successful [Outcome] with an empty list means nothing was
 * recognized; a [Outcome.Failure] distinguishes an offline device from other errors.
 */
interface TextRecognitionRepository {

    suspend fun recognize(image: Uri): Outcome<List<RecognizedTextLine>, AiError>
}
