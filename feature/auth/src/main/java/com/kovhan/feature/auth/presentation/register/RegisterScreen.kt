package com.kovhan.feature.auth.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonAccent
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.core.ui.component.button.QuotifyButtonVariant
import com.kovhan.core.ui.component.text.ErrorText
import com.kovhan.core.ui.component.text_field.QuotifyPasswordField
import com.kovhan.core.ui.component.text_field.QuotifyTextField
import com.kovhan.core.ui.extensions.orEmpty
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.domain.auth.model.ValidationError
import com.kovhan.feature.auth.presentation.component.AuthAltRow
import com.kovhan.feature.auth.presentation.component.AuthOrDivider
import com.kovhan.feature.auth.presentation.component.AuthScreenScaffold
import com.kovhan.feature.auth.presentation.component.AuthTitleBlock
import com.kovhan.feature.auth.presentation.component.GoogleAuthButton
import com.kovhan.feature.auth.presentation.register.component.TermsCheckboxText
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenIntent
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenState
import com.kovhan.feature.auth.presentation.register.navigation.RegisterScreenNavAction
import com.kovhan.feature.auth.presentation.util.toSnackbar

@Composable
fun RegisterScreen(
    state: RegisterScreenState,
    intent: RegisterScreenIntent,
    navAction: RegisterScreenNavAction,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val dimens = QuotifyMaterialTheme.dimensions
    val errorValidationMessage = remember {
        derivedStateOf {
            state.errorValidationMessage?.toSnackbar()?.messageRes
        }
    }

    AuthScreenScaffold(
        onBack = intent::onBackClicked,
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        footer = {
            AuthAltRow(
                question = stringResource(R.string.sign_up_alt_q),
                action = stringResource(R.string.sign_up_alt_cta),
                onActionClick = intent::onSignInClicked,
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AuthTitleBlock(
                title = stringResource(R.string.sign_up_title),
                subtitle = stringResource(R.string.sign_up_subtitle),
            )

            Spacer(Modifier.height(18.dp))

            QuotifyTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.fullName,
                label = stringResource(R.string.sign_up_full_name),
                placeholder = stringResource(R.string.sign_up_full_name_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = intent::onFullNameChanged,
            )

            Spacer(Modifier.height(dimens.space3))

            QuotifyTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.username,
                label = stringResource(R.string.sign_up_username),
                required = true,
                placeholder = stringResource(R.string.sign_up_username_placeholder),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                onValueChange = intent::onUsernameChanged,
                error = if(state.errorValidationMessage is ValidationError.EmptyName)
                    errorValidationMessage.value
                else null,
            )

            Spacer(Modifier.height(dimens.space3))

            QuotifyTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.email,
                label = stringResource(R.string.auth_email),
                required = true,
                placeholder = stringResource(R.string.sign_up_email_placeholder),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                onValueChange = intent::onEmailChanged,
                error = if(state.errorValidationMessage is ValidationError.InvalidEmail)
                    errorValidationMessage.value
                else null,
            )

            Spacer(Modifier.height(dimens.space3))

            QuotifyPasswordField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                label = stringResource(R.string.auth_password),
                required = true,
                placeholder = stringResource(R.string.sign_up_password_placeholder),
                imeAction = ImeAction.Done,
                onValueChange = intent::onPasswordChanged,
                error = if(state.errorValidationMessage is ValidationError.ShortPassword)
                    errorValidationMessage.value
                else null
            )

            Spacer(Modifier.height(dimens.space3))

            TermsCheckboxText(
                modifier = Modifier.fillMaxWidth(),
                accepted = state.termsAccepted,
                onToggle = intent::onTermsToggled,
                onPrivacyPolicyClick = intent::onPrivacyPolicyClicked,
                onTermsOfServiceClick = intent::onTermsOfServiceClicked,
            )

            if (state.errorMessage != null) {
                ErrorText(
                    modifier = Modifier,
                    error = state.errorMessage.toSnackbar().messageRes.orEmpty(),
                    paddingTop = dimens.space4
                )
            }

            Spacer(Modifier.height(dimens.space4))

            QuotifyButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.sign_up_cta),
                onClick = intent::onSignUpClicked,
                enabled = state.canSubmit,
                loading = state.isLoading,
                variant = QuotifyButtonVariant.Filled,
                accent = QuotifyButtonAccent.Primary,
                sizeSpec = QuotifyButtonDefaults.pillSizeSpec(),
            )

            Spacer(Modifier.height(14.dp))
            AuthOrDivider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(14.dp))

            GoogleAuthButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = intent::onGoogleSignInClicked,
                enabled = !state.isLoading,
                loading = state.isGoogleLoading,
            )
        }
    }
}
