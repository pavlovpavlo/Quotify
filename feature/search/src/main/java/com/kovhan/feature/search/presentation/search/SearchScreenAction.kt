package com.kovhan.feature.search.presentation.search

import androidx.compose.runtime.Stable
import com.kovhan.feature.search.presentation.search.mvi.SearchScope

@Stable
interface SearchScreenAction {
    fun onQueryChange(query: String)
    fun onScopeChange(scope: SearchScope)

    companion object {
        val Empty: SearchScreenAction = EmptySearchScreenAction
    }
}

private object EmptySearchScreenAction : SearchScreenAction {
    override fun onQueryChange(query: String) = Unit
    override fun onScopeChange(scope: SearchScope) = Unit
}
