package com.kovhan.core.analytics.event

import com.kovhan.core.analytics.AnalyticsEvent
import com.kovhan.core.analytics.AnalyticsParam
import com.kovhan.core.analytics.SurveyExitChoice
import com.kovhan.core.analytics.SurveyInviteChoice

class SurveyInviteShown(surveyId: String) : AnalyticsEvent(
    name = "survey_invite_shown",
    params = mapOf(AnalyticsParam.SURVEY_ID to surveyId),
)

class SurveyInviteAnswered(surveyId: String, choice: SurveyInviteChoice) : AnalyticsEvent(
    name = "survey_invite_answered",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.REASON to choice.value,
    ),
)

class SurveyStarted(surveyId: String) : AnalyticsEvent(
    name = "survey_started",
    params = mapOf(AnalyticsParam.SURVEY_ID to surveyId),
)

class SurveyQuestionAnswered(
    surveyId: String,
    questionId: String,
    optionsCount: Int,
    hasComment: Boolean,
) : AnalyticsEvent(
    name = "survey_question_answered",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.QUESTION_ID to questionId,
        AnalyticsParam.OPTIONS_COUNT to optionsCount,
        AnalyticsParam.HAS_COMMENT to hasComment,
    ),
)

class SurveyQuestionSkipped(surveyId: String, questionId: String) : AnalyticsEvent(
    name = "survey_question_skipped",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.QUESTION_ID to questionId,
    ),
)

class SurveyCompleted(surveyId: String, answeredCount: Int) : AnalyticsEvent(
    name = "survey_completed",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.ANSWERED_COUNT to answeredCount,
    ),
)

class SurveyAbandoned(surveyId: String, step: Int, choice: SurveyExitChoice) : AnalyticsEvent(
    name = "survey_abandoned",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.STEP to step,
        AnalyticsParam.REASON to choice.value,
    ),
)

class SurveyResumed(surveyId: String, step: Int) : AnalyticsEvent(
    name = "survey_resumed",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.STEP to step,
    ),
)

class SurveyStepBack(surveyId: String, step: Int) : AnalyticsEvent(
    name = "survey_step_back",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.STEP to step,
    ),
)

class SurveyRewardGranted(surveyId: String, coverId: String) : AnalyticsEvent(
    name = "survey_reward_granted",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.REWARD_ID to coverId,
    ),
)

/** Every reward cover is handed out — the thank-you screen apologises instead. */
class SurveyRewardDepleted(surveyId: String, completedSurveys: Int) : AnalyticsEvent(
    name = "survey_reward_depleted",
    params = mapOf(
        AnalyticsParam.SURVEY_ID to surveyId,
        AnalyticsParam.COMPLETED_SURVEYS to completedSurveys,
    ),
)
