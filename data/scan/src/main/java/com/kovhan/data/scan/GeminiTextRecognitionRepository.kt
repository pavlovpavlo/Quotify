package com.kovhan.data.scan

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.kovhan.core.models.AiError
import com.kovhan.core.models.Outcome
import com.kovhan.domain.connectivity.ConnectivityRepository
import com.kovhan.domain.scan.TextRecognitionRepository
import com.kovhan.domain.scan.model.RecognizedTextLine
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * [TextRecognitionRepository] backed by Gemini through Firebase AI Logic.
 *
 * The photo is sent to a multimodal Gemini model that reads the text — Ukrainian
 * or English — and returns it cleaned of the usual OCR artifacts. The model is
 * asked to transcribe only, never translate or rephrase, so the output stays
 * faithful to the source. Recognition needs the network; an empty list means
 * nothing readable was found or the request failed.
 */
class GeminiTextRecognitionRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val connectivity: ConnectivityRepository,
) : TextRecognitionRepository {

    private val model by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(MODEL_NAME)
    }

    override suspend fun recognize(image: Uri): Outcome<List<RecognizedTextLine>, AiError> =
        withContext(Dispatchers.IO) {
            if (!connectivity.isOnline()) return@withContext Outcome.Failure(AiError.Offline)
            val bitmap = decodeBitmap(image)
                ?: return@withContext Outcome.Failure(AiError.Unknown)
            try {
                val request = content {
                    image(bitmap)
                    text(PROMPT)
                }
                val recognized = model.generateContent(request).text.orEmpty().trim()
                val lines = if (recognized.isEmpty() || recognized == EMPTY_MARKER) {
                    emptyList()
                } else {
                    recognized.split('\n')
                        .map(String::trim)
                        .filter(String::isNotEmpty)
                        .map(::RecognizedTextLine)
                }
                Outcome.Success(lines)
            } catch (t: Throwable) {
                Timber.e(t, "Gemini text recognition failed")
                Outcome.Failure(AiError.Unknown)
            } finally {
                bitmap.recycle()
            }
        }

    private fun decodeBitmap(image: Uri): Bitmap? = runCatching {
        val source = ImageDecoder.createSource(context.contentResolver, image)
        ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = false
            val longest = maxOf(info.size.width, info.size.height)
            if (longest > MAX_DIMENSION) decoder.setTargetSampleSize(sampleSizeFor(longest))
        }
    }.onFailure { Timber.e(it, "Failed to decode image $image") }.getOrNull()

    private fun sampleSizeFor(longest: Int): Int {
        var sample = 1
        while (longest / sample > MAX_DIMENSION) sample *= 2
        return sample
    }

    private companion object {
        const val MODEL_NAME = "gemini-2.5-flash"
        const val MAX_DIMENSION = 1536
        const val EMPTY_MARKER = "NO_TEXT"
        val PROMPT = """
            You are an OCR engine. Transcribe every piece of readable text in this image
            exactly as it appears. The text may be in Ukrainian or English.
            Fix obvious OCR-style artifacts and restore the correct letters, but do NOT
            translate, rephrase, summarize, or invent anything that is not in the image.
            Keep the original line breaks. Return only the transcribed text, with no
            commentary and no surrounding quotes.
            If the image contains no readable text, respond with exactly: NO_TEXT
        """.trimIndent()
    }
}
