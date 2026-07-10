package com.kovhan.data.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.kovhan.domain.voice.VoiceInputRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class AndroidVoiceInputRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : VoiceInputRepository {

    override fun isAvailable(): Boolean =
        SpeechRecognizer.isRecognitionAvailable(context)

    override fun transcript(): Flow<String> = callbackFlow {
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val handler = Handler(Looper.getMainLooper())

        // Finalized segments joined so far. Android's SpeechRecognizer ends the
        // session on each silence gap, so we restart it and keep appending.
        var finalText = ""
        var stopped = false

        fun buildIntent(): Intent =
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                )
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            }

        fun firstResult(bundle: Bundle?): String? =
            bundle
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                ?.trim()
                ?.takeIf { it.isNotEmpty() }

        fun merge(base: String, segment: String): String =
            if (base.isEmpty()) segment else "$base $segment"

        fun restart(delayMs: Long = 0L) {
            if (stopped) return
            handler.postDelayed({
                if (!stopped) {
                    runCatching { recognizer.startListening(buildIntent()) }
                }
            }, delayMs)
        }

        val listener = object : RecognitionListener {
            override fun onPartialResults(partialResults: Bundle?) {
                val segment = firstResult(partialResults) ?: return
                trySend(merge(finalText, segment))
            }

            override fun onResults(results: Bundle?) {
                firstResult(results)?.let { segment ->
                    finalText = merge(finalText, segment)
                    trySend(finalText)
                }
                // Session ended after this utterance — keep listening.
                restart()
            }

            override fun onError(error: Int) {
                when (error) {
                    // Only a missing permission is truly fatal — everything else
                    // (silence timeout, no match, busy) just means "resume listening".
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> close()
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> restart(delayMs = 120L)
                    else -> restart(delayMs = 60L)
                }
            }

            override fun onReadyForSpeech(params: Bundle?) = Unit
            override fun onBeginningOfSpeech() = Unit
            override fun onRmsChanged(rmsdB: Float) = Unit
            override fun onBufferReceived(buffer: ByteArray?) = Unit
            override fun onEndOfSpeech() = Unit
            override fun onEvent(eventType: Int, params: Bundle?) = Unit
        }

        recognizer.setRecognitionListener(listener)
        recognizer.startListening(buildIntent())

        awaitClose {
            stopped = true
            handler.removeCallbacksAndMessages(null)
            recognizer.stopListening()
            recognizer.destroy()
        }
    }.flowOn(Dispatchers.Main.immediate)
}
