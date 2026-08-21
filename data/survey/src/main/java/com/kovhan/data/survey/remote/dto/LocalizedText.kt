package com.kovhan.data.survey.remote.dto

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

/** Locale tag to copy, e.g. `{"en": "Skip", "uk": "Пропустити"}`. */
internal typealias LocalizedText = Map<String, String>

internal const val FALLBACK_LANGUAGE = "en"

internal fun LocalizedText.resolve(language: String): String? =
    get(language) ?: get(FALLBACK_LANGUAGE) ?: values.firstOrNull()

/**
 * Accepts either a locale map or a bare string — a single-language survey can
 * be written as `"title": "..."` and still parse.
 */
internal object LocalizedTextSerializer :
    JsonTransformingSerializer<LocalizedText>(
        MapSerializer(String.serializer(), String.serializer()),
    ) {
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive) {
            JsonObject(mapOf(FALLBACK_LANGUAGE to element))
        } else {
            element
        }
}
