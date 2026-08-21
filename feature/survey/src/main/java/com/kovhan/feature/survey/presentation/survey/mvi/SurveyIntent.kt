package com.kovhan.feature.survey.presentation.survey.mvi

interface SurveyIntent {
    fun onOptionClicked(questionId: String, optionId: String)
    fun onInputChanged(questionId: String, optionId: String, text: String)
    fun onNextClicked()
    fun onSkipClicked()
    fun onBackClicked()
    fun onExitClicked()
    fun onRewardPrimaryClicked()
    fun onRewardSecondaryClicked()

    companion object {
        val Empty: SurveyIntent = object : SurveyIntent {
            override fun onOptionClicked(questionId: String, optionId: String) = Unit
            override fun onInputChanged(questionId: String, optionId: String, text: String) = Unit
            override fun onNextClicked() = Unit
            override fun onSkipClicked() = Unit
            override fun onBackClicked() = Unit
            override fun onExitClicked() = Unit
            override fun onRewardPrimaryClicked() = Unit
            override fun onRewardSecondaryClicked() = Unit
        }
    }
}
