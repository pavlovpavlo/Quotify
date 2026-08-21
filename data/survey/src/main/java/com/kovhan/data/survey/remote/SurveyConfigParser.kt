package com.kovhan.data.survey.remote

import com.kovhan.core.models.survey.Survey
import com.kovhan.core.models.survey.SurveyInvite
import com.kovhan.core.models.survey.SurveyOption
import com.kovhan.core.models.survey.SurveyOptionInput
import com.kovhan.core.models.survey.SurveyQuestion
import com.kovhan.core.models.survey.SurveyQuestionType
import com.kovhan.core.models.survey.SurveyReward
import com.kovhan.core.models.survey.SurveyRewardKind
import com.kovhan.data.survey.remote.dto.SurveyDto
import com.kovhan.data.survey.remote.dto.SurveyOptionDto
import com.kovhan.data.survey.remote.dto.SurveyQuestionDto
import com.kovhan.data.survey.remote.dto.SurveysConfigDto
import com.kovhan.data.survey.remote.dto.resolve
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Turns the raw `android_surveys` payload into surveys for one language.
 * Anything malformed is dropped rather than thrown — a broken config must never
 * take the app down, it just means no survey is offered.
 */
@Singleton
class SurveyConfigParser @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    fun parse(raw: String, language: String): List<Survey> {
        if (raw.isBlank()) return emptyList()

        val config = runCatching { json.decodeFromString<SurveysConfigDto>(raw) }
            .onFailure { Timber.w(it, "Survey config is not parseable") }
            .getOrNull() ?: return emptyList()

        // Вища версія схеми — привід насторожитись, а не мовчки сховати опитування:
        // поле легко сплутати з версією самого опитування. Невідомі ключі парсер
        // ігнорує, а зламані питання й так відсіюються нижче.
        if (config.schemaVersion > SurveysConfigDto.SUPPORTED_SCHEMA_VERSION) {
            Timber.w(
                "Survey config schema %d is newer than supported %d — parsing what we can",
                config.schemaVersion,
                SurveysConfigDto.SUPPORTED_SCHEMA_VERSION,
            )
        }

        return config.surveys
            .mapNotNull { it.toSurvey(language) }
            .distinctBy { it.id }
    }

    private fun SurveyDto.toSurvey(language: String): Survey? {
        val surveyId = id?.trim().orEmpty()
        if (surveyId.isEmpty()) return null

        val resolvedQuestions = questions
            .mapNotNull { it.toQuestion(language) }
            .distinctBy { it.id }
        if (resolvedQuestions.isEmpty()) return null

        return Survey(
            id = surveyId,
            enabled = enabled,
            showProgress = showProgress,
            invite = invite?.let {
                SurveyInvite(
                    title = it.title.resolve(language),
                    body = it.body.resolve(language),
                    reward = it.reward.resolve(language),
                )
            },
            reward = reward?.id?.takeIf { it.isNotBlank() }?.let {
                SurveyReward(id = it, kind = SurveyRewardKind.fromRaw(reward.kind))
            },
            questions = resolvedQuestions,
        )
    }

    private fun SurveyQuestionDto.toQuestion(language: String): SurveyQuestion? {
        val questionId = id?.trim().orEmpty()
        val resolvedTitle = title.resolve(language)?.trim().orEmpty()
        if (questionId.isEmpty() || resolvedTitle.isEmpty()) return null

        val resolvedOptions = options
            .mapNotNull { it.toOption(language) }
            .distinctBy { it.id }
        if (resolvedOptions.isEmpty()) return null

        return SurveyQuestion(
            id = questionId,
            title = resolvedTitle,
            description = description.resolve(language)?.takeIf { it.isNotBlank() },
            type = if (multiSelect) SurveyQuestionType.MULTI else SurveyQuestionType.SINGLE,
            skippable = skippable,
            options = resolvedOptions,
        )
    }

    private fun SurveyOptionDto.toOption(language: String): SurveyOption? {
        val optionId = id?.trim().orEmpty()
        val resolvedText = text.resolve(language)?.trim().orEmpty()
        if (optionId.isEmpty() || resolvedText.isEmpty()) return null

        return SurveyOption(
            id = optionId,
            text = resolvedText,
            input = input?.let {
                SurveyOptionInput(
                    required = it.required,
                    label = it.label.resolve(language)?.takeIf(String::isNotBlank),
                    placeholder = it.placeholder.resolve(language)?.takeIf(String::isNotBlank),
                )
            },
        )
    }
}
