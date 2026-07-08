package com.kovhan.domain.scan

import android.net.Uri
import com.kovhan.domain.scan.model.RecognizedTextLine

/**
 * Abstraction over on-device optical character recognition.
 *
 * [recognize] decodes the image behind [image] and returns the text it finds,
 * split into lines. It is a suspending one-shot: no lifecycle to manage from the
 * caller. An empty list means nothing was recognized.
 */
interface TextRecognitionRepository {

    suspend fun recognize(image: Uri): List<RecognizedTextLine>
}
