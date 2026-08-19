package com.kovhan.feature.addquote.presentation.addquote.navigation

import com.kovhan.core.navigation.models.QuoteInputMethod
import androidx.compose.runtime.Stable

@Stable
interface AddQuoteScreenNavAction {
    fun close()

    fun proceedToDetails(quote: String, inputMethod: QuoteInputMethod)

    fun openPaywall()

    companion object {
        val Empty: AddQuoteScreenNavAction = object : AddQuoteScreenNavAction {
            override fun close() = Unit
            override fun proceedToDetails(quote: String, inputMethod: QuoteInputMethod) = Unit
            override fun openPaywall() = Unit
        }
    }
}
