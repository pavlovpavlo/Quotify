package com.kovhan.feature.survey.presentation.done

/** What the thank-you screen has to hand over once the survey is finished. */
sealed interface SurveyRewardState {

    data object None : SurveyRewardState

    /** A reward cover the user just earned. */
    data class Granted(val coverId: String) : SurveyRewardState

    /** Every reward cover is already handed out. */
    data object Depleted : SurveyRewardState
}
