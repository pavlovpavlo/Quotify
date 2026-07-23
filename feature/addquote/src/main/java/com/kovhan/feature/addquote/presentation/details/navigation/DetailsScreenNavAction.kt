package com.kovhan.feature.addquote.presentation.details.navigation

import androidx.compose.runtime.Stable
import com.kovhan.feature.addquote.presentation.details.mvi.QuoteDraft

@Stable
interface DetailsScreenNavAction {
    fun back()

    fun close()

    fun openTagSheet()

    fun proceedToSave(draft: QuoteDraft)

    companion object {
        val Empty: DetailsScreenNavAction = object : DetailsScreenNavAction {
            override fun back() = Unit
            override fun close() = Unit
            override fun openTagSheet() = Unit
            override fun proceedToSave(draft: QuoteDraft) = Unit
        }
    }
}
