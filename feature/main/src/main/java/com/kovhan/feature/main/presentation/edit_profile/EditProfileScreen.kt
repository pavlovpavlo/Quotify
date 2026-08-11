package com.kovhan.feature.main.presentation.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.edit_profile.component.EditAvatar
import com.kovhan.feature.main.presentation.edit_profile.component.EditCard
import com.kovhan.feature.main.presentation.edit_profile.component.EditRow
import com.kovhan.feature.main.presentation.edit_profile.component.EditSectionTitle
import com.kovhan.core.navigation.EditField
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileIntent
import com.kovhan.feature.main.presentation.edit_profile.mvi.EditProfileState
import com.kovhan.feature.main.presentation.edit_profile.navigation.EditProfileScreenNavAction

@Composable
fun EditProfileScreen(
    state: EditProfileState,
    intent: EditProfileIntent,
    navAction: EditProfileScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val isGoogleAccount = state.user?.isGoogleAccount == true

    val displayName = state.user?.displayName?.takeIf { it.isNotBlank() }.orEmpty()
    val isAnonymous = state.user?.isAnonymous == true
    val email = state.user?.email.orEmpty()
    val username = state.user?.username
        ?: if (isGoogleAccount) email.substringBefore("@").takeIf { it.isNotBlank() }.orEmpty() else ""

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bgPrimary)
                .padding(top = paddingValues.calculateTopPadding()),
        ) {
            QuotifyTopBar(
                title = stringResource(R.string.edit_profile_title),
                onBack = navAction::navigateBack,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = 22.dp,
                        end = 22.dp,
                        top = 4.dp,
                        bottom = paddingValues.calculateBottomPadding() + 40.dp,
                    ),
            ) {
                EditAvatar(
                    photoUrl = state.user?.photoUrl,
                    onCameraClick = intent::onPhotoClicked,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp),
                )

                EditCard {
                    EditRow(
                        iconRes = R.drawable.ic_user,
                        label = stringResource(R.string.edit_field_name),
                        value = displayName,
                        showDivider = !isAnonymous,
                        onClick = { intent.onFieldClicked(EditField.NAME) },
                    )
                    if (!isAnonymous) {
                        EditRow(
                            iconRes = R.drawable.ic_at_sign,
                            label = stringResource(R.string.edit_field_username),
                            value = if (username.isNotBlank()) "@$username" else "",
                            showDivider = !isGoogleAccount,
                            onClick = { intent.onFieldClicked(EditField.USERNAME) },
                        )
                    }
                    if (!isGoogleAccount && !isAnonymous) {
                        EditRow(
                            iconRes = R.drawable.ic_mail,
                            label = stringResource(R.string.edit_field_email),
                            value = email,
                            showDivider = false,
                            onClick = { intent.onFieldClicked(EditField.EMAIL) },
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                EditSectionTitle(stringResource(R.string.edit_account_section))

                EditCard {
                    if (isAnonymous) {
                        EditRow(
                            iconRes = R.drawable.ic_log_out,
                            label = stringResource(R.string.edit_sign_in_or_register),
                            value = "",
                            showDivider = true,
                            onClick = intent::onSignInOrRegisterClicked,
                        )
                    } else {
                        if (!isGoogleAccount) {
                            EditRow(
                                iconRes = R.drawable.ic_lock,
                                label = stringResource(R.string.edit_change_password),
                                value = "",
                                showDivider = true,
                                onClick = intent::onPasswordClicked,
                            )
                        }
                        EditRow(
                            iconRes = R.drawable.ic_log_out,
                            label = stringResource(R.string.edit_sign_out),
                            value = "",
                            showDivider = true,
                            onClick = intent::onLogoutClicked,
                        )
                    }
                    EditRow(
                        iconRes = R.drawable.ic_trash,
                        label = stringResource(R.string.edit_delete_account),
                        value = "",
                        showDivider = false,
                        danger = true,
                        onClick = intent::onDeleteAccountClicked,
                    )
                }
            }
        }

        if (state.isProcessing) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = colors.accentPrimary)
            }
        }
    }
}
