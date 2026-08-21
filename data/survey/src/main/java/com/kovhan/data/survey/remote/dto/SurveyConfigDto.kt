package com.kovhan.data.survey.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SurveysConfigDto(
    val schemaVersion: Int = SUPPORTED_SCHEMA_VERSION,
    val surveys: List<SurveyDto> = emptyList(),
) {
    companion object {
        const val SUPPORTED_SCHEMA_VERSION = 1
    }
}

@Serializable
internal data class SurveyDto(
    val id: String? = null,
    val enabled: Boolean = true,
    val showProgress: Boolean = true,
    val invite: SurveyInviteDto? = null,
    val reward: SurveyRewardDto? = null,
    val questions: List<SurveyQuestionDto> = emptyList(),
)

@Serializable
internal data class SurveyInviteDto(
    @Serializable(with = LocalizedTextSerializer::class)
    val title: LocalizedText = emptyMap(),
    @Serializable(with = LocalizedTextSerializer::class)
    val body: LocalizedText = emptyMap(),
    @Serializable(with = LocalizedTextSerializer::class)
    val reward: LocalizedText = emptyMap(),
)

@Serializable
internal data class SurveyRewardDto(
    val id: String? = null,
    val kind: String? = null,
)

@Serializable
internal data class SurveyQuestionDto(
    val id: String? = null,
    @Serializable(with = LocalizedTextSerializer::class)
    val title: LocalizedText = emptyMap(),
    @Serializable(with = LocalizedTextSerializer::class)
    val description: LocalizedText = emptyMap(),
    @SerialName("multiSelect")
    val multiSelect: Boolean = false,
    val skippable: Boolean = true,
    val options: List<SurveyOptionDto> = emptyList(),
)

@Serializable
internal data class SurveyOptionDto(
    val id: String? = null,
    @Serializable(with = LocalizedTextSerializer::class)
    val text: LocalizedText = emptyMap(),
    val input: SurveyOptionInputDto? = null,
)

@Serializable
internal data class SurveyOptionInputDto(
    val required: Boolean = false,
    @Serializable(with = LocalizedTextSerializer::class)
    val label: LocalizedText = emptyMap(),
    @Serializable(with = LocalizedTextSerializer::class)
    val placeholder: LocalizedText = emptyMap(),
)
