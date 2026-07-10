package com.kovhan.domain.scan.use_case

import android.net.Uri
import com.kovhan.core.models.AiError
import com.kovhan.core.models.Outcome
import com.kovhan.domain.scan.TextRecognitionRepository
import com.kovhan.domain.scan.model.RecognizedTextLine
import javax.inject.Inject

class RecognizeTextUseCase @Inject constructor(
    private val repository: TextRecognitionRepository,
) {
    suspend operator fun invoke(image: Uri): Outcome<List<RecognizedTextLine>, AiError> =
        repository.recognize(image)
}
