package com.kovhan.feature.auth.presentation.forgot_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.text_field.QuotifyTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.text.ErrorText
import com.kovhan.core.ui.extensions.orEmpty
import com.kovhan.feature.auth.presentation.component.AuthScreenScaffold
import com.kovhan.feature.auth.presentation.component.AuthTextLink
import com.kovhan.feature.auth.presentation.component.AuthTitleBlock
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenIntent
import com.kovhan.feature.auth.presentation.forgot_password.mvi.ForgotPasswordScreenState
import com.kovhan.feature.auth.presentation.forgot_password.navigation.ForgotPasswordScreenNavAction
import com.kovhan.feature.auth.presentation.util.toSnackbar

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordScreenState,
    intent: ForgotPasswordScreenIntent,
    navAction: ForgotPasswordScreenNavAction,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val colors = QuotifyMaterialTheme.colors
    val dimens = QuotifyMaterialTheme.dimensions

    AuthScreenScaffold(
        onBack = intent::onBackClicked,
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        footer = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AuthTextLink(
                    text = stringResource(R.string.forgot_back_to_sign_in),
                    onClick = intent::onBackToSignInClicked,
                )
            }
        },
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(colors.accentPrimarySoft),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                colorFilter = ColorFilter.tint(colors.accentPrimary),
            )
        }

        Spacer(Modifier.height(14.dp))

        AuthTitleBlock(
            title = stringResource(R.string.forgot_title),
            subtitle = stringResource(R.string.forgot_subtitle),
        )

        Spacer(Modifier.height(18.dp))

        QuotifyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            label = stringResource(R.string.auth_email),
            placeholder = stringResource(R.string.forgot_email_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            onValueChange = intent::onEmailChanged,
            error = state.errorValidationMessage?.toSnackbar()?.messageRes
        )

        if (state.errorMessage != null) {
            ErrorText(modifier = Modifier,
                error = state.errorMessage.toSnackbar().messageRes.orEmpty(),
                paddingTop = dimens.space4)
        }

        Spacer(Modifier.height(dimens.space4))

        QuotifyButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.forgot_cta),
            onClick = intent::onSendClicked,
            enabled = state.canSubmit,
            loading = state.isLoading,
            variant = QuotifyButtonVariant.Filled,
            accent = QuotifyButtonAccent.Primary,
            sizeSpec = QuotifyButtonDefaults.pillSizeSpec(),
        )
    }
}
