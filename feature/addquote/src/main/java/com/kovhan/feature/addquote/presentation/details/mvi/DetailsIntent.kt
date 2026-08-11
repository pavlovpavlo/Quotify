package com.kovhan.feature.addquote.presentation.details.mvi

import androidx.compose.ui.text.input.TextFieldValue

interface DetailsIntent {
    fun onInitialQuote(text: String)
    fun onQuoteChanged(value: TextFieldValue)

    fun onAuthorQueryChanged(value: String)
    fun onAuthorPicked(name: String)
    fun onBookQueryChanged(value: String)
    fun onBookPicked(name: String)
    fun onPageChanged(value: String)

    fun onOpenTagSheet()
    fun onRemoveTag(tag: String)

    fun onWidgetToggle(enabled: Boolean)
    fun onPushToggle(enabled: Boolean)

    fun onSaveClicked()
    fun onBackClicked()
    fun onCloseClicked()

    companion object {
        val Empty: DetailsIntent = object : DetailsIntent {
            override fun onInitialQuote(text: String) = Unit
            override fun onQuoteChanged(value: TextFieldValue) = Unit
            override fun onAuthorQueryChanged(value: String) = Unit
            override fun onAuthorPicked(name: String) = Unit
            override fun onBookQueryChanged(value: String) = Unit
            override fun onBookPicked(name: String) = Unit
            override fun onPageChanged(value: String) = Unit
            override fun onOpenTagSheet() = Unit
            override fun onRemoveTag(tag: String) = Unit
            override fun onWidgetToggle(enabled: Boolean) = Unit
            override fun onPushToggle(enabled: Boolean) = Unit
            override fun onSaveClicked() = Unit
            override fun onBackClicked() = Unit
            override fun onCloseClicked() = Unit
        }
    }
}
