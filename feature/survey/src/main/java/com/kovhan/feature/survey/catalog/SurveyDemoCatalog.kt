package com.kovhan.feature.survey.catalog

import android.content.Context
import com.kovhan.core.models.survey.Survey
import com.kovhan.core.models.survey.SurveyOption
import com.kovhan.core.models.survey.SurveyOptionInput
import com.kovhan.core.models.survey.SurveyQuestion
import com.kovhan.core.models.survey.SurveyQuestionType
import com.kovhan.core.navigation.DEFAULT_SURVEY_ID
import com.kovhan.design.systems.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bundled questionnaire used when Remote Config has nothing for the requested
 * id — it keeps the screens openable from dev tools and previews. The invite
 * flow never falls back to it: it only offers surveys that come from the config.
 */
@Singleton
class SurveyDemoCatalog @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun byId(surveyId: String): Survey? =
        if (surveyId == DEFAULT_SURVEY_ID) demo(surveyId) else null

    private fun demo(surveyId: String) = Survey(
        id = surveyId,
        questions = listOf(usage(), value(), missing(), nps()),
    )

    private fun usage() = SurveyQuestion(
        id = "usage",
        title = string(R.string.survey_q_usage_title),
        description = string(R.string.survey_q_usage_hint),
        type = SurveyQuestionType.SINGLE,
        options = listOf(
            option("daily", R.string.survey_q_usage_daily),
            option("weekly", R.string.survey_q_usage_weekly),
            option("monthly", R.string.survey_q_usage_monthly),
            option("rarely", R.string.survey_q_usage_rarely),
        ),
    )

    private fun value() = SurveyQuestion(
        id = "value",
        title = string(R.string.survey_q_value_title),
        description = string(R.string.survey_q_value_hint),
        type = SurveyQuestionType.MULTI,
        options = listOf(
            option("saving", R.string.survey_q_value_saving),
            option("collections", R.string.survey_q_value_collections),
            option("widget", R.string.survey_q_value_widget),
            option("reminders", R.string.survey_q_value_reminders),
            option("search", R.string.survey_q_value_search),
        ),
    )

    private fun missing() = SurveyQuestion(
        id = "missing",
        title = string(R.string.survey_q_missing_title),
        description = string(R.string.survey_q_missing_hint),
        type = SurveyQuestionType.SINGLE,
        options = listOf(
            option("sync", R.string.survey_q_missing_sync),
            option("styles", R.string.survey_q_missing_styles),
            option("shared", R.string.survey_q_missing_shared),
            option("export", R.string.survey_q_missing_export),
            option(
                id = "other",
                textRes = R.string.survey_q_missing_other,
                input = SurveyOptionInput(
                    required = true,
                    label = string(R.string.survey_q_missing_comment_label),
                    placeholder = string(R.string.survey_q_missing_comment_hint),
                ),
            ),
        ),
    )

    private fun nps() = SurveyQuestion(
        id = "nps",
        title = string(R.string.survey_q_nps_title),
        description = string(R.string.survey_q_nps_hint),
        type = SurveyQuestionType.SINGLE,
        options = listOf(
            option("definitely", R.string.survey_q_nps_definitely),
            option("probably", R.string.survey_q_nps_probably),
            option("unsure", R.string.survey_q_nps_unsure),
            option(
                id = "no",
                textRes = R.string.survey_q_nps_no,
                input = SurveyOptionInput(
                    required = false,
                    label = string(R.string.survey_q_nps_comment_label),
                    placeholder = string(R.string.survey_q_nps_comment_hint),
                ),
            ),
        ),
    )

    private fun option(id: String, textRes: Int, input: SurveyOptionInput? = null) =
        SurveyOption(id = id, text = string(textRes), input = input)

    private fun string(resId: Int): String = context.getString(resId)
}
