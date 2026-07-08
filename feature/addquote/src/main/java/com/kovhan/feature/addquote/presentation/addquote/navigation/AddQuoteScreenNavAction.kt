package com.kovhan.feature.addquote.presentation.addquote.navigation

import androidx.compose.runtime.Stable

@Stable
interface AddQuoteScreenNavAction {
    fun close()

    fun proceedToDetails(quote: String)

    companion object {
        val Empty: AddQuoteScreenNavAction = object : AddQuoteScreenNavAction {
            override fun close() = Unit
            override fun proceedToDetails(quote: String) = Unit
        }
    }
}
