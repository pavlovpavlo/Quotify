package com.kovhan.feature.search.presentation.search.navigation

import androidx.compose.runtime.Stable
import com.kovhan.core.navigation.EntityType

@Stable
interface SearchScreenNavAction {
    fun onClose()
    fun openFolder(collectionId: String)
    fun openEntity(type: EntityType, entityId: String, title: String)

    companion object {
        val Empty: SearchScreenNavAction = EmptySearchScreenNavAction
    }
}

private object EmptySearchScreenNavAction : SearchScreenNavAction {
    override fun onClose() = Unit
    override fun openFolder(collectionId: String) = Unit
    override fun openEntity(type: EntityType, entityId: String, title: String) = Unit
}
