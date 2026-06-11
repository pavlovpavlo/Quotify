package com.kovhan.feature.main.presentation.edit_profile.mvi

import com.kovhan.core.navigation.EditField

interface EditProfileIntent {
    fun onPhotoClicked()
    fun onFieldClicked(field: EditField)
    fun onPasswordClicked()

    fun onFieldSaved(field: EditField, value: String)
    fun onPhotoPicked(uri: String)
    fun onPhotoRemoved()

    fun onLogoutClicked()
    fun onDeleteAccountClicked()
    fun onLogoutConfirmed()
    fun onDeleteConfirmed()

    companion object {
        val Empty: EditProfileIntent = object : EditProfileIntent {
            override fun onPhotoClicked() = Unit
            override fun onFieldClicked(field: EditField) = Unit
            override fun onPasswordClicked() = Unit
            override fun onFieldSaved(field: EditField, value: String) = Unit
            override fun onPhotoPicked(uri: String) = Unit
            override fun onPhotoRemoved() = Unit
            override fun onLogoutClicked() = Unit
            override fun onDeleteAccountClicked() = Unit
            override fun onLogoutConfirmed() = Unit
            override fun onDeleteConfirmed() = Unit
        }
    }
}
