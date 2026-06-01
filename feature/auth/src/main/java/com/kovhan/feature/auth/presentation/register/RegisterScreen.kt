package com.kovhan.feature.auth.presentation.register

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.text_field.QuotifyPasswordField
import com.kovhan.core.ui.component.text_field.QuotifyTextField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.design.systems.component.button.QuotifyButtonAccent
import com.kovhan.design.systems.component.button.QuotifyButtonDefaults
import com.kovhan.design.systems.component.button.QuotifyButtonVariant
import com.kovhan.feature.auth.presentation.component.AuthAltRow
import com.kovhan.feature.auth.presentation.component.AuthOrDivider
import com.kovhan.feature.auth.presentation.component.AuthScreenScaffold
import com.kovhan.feature.auth.presentation.component.AuthTitleBlock
import com.kovhan.feature.auth.presentation.component.GoogleAuthButton
import com.kovhan.feature.auth.presentation.google.rememberGoogleSignIn
import com.kovhan.feature.auth.presentation.register.component.TermsCheckboxText
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenIntent
import com.kovhan.feature.auth.presentation.register.mvi.RegisterScreenState
import com.kovhan.feature.auth.presentation.register.navigation.RegisterScreenNavAction

@Composable
fun RegisterScreen(
    state: RegisterScreenState,
    intent: RegisterScreenIntent,
    navAction: RegisterScreenNavAction,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val dimens = QuotifyMaterialTheme.dimensions
    val launchGoogleSignIn = rememberGoogleSignIn(
        onStart = intent::onGoogleSignInStarted,
        onIdToken = intent::onGoogleIdTokenReceived,
        onError = intent::onGoogleSignInFailed,
    )

    AuthScreenScaffold(
        onBack = intent::onBackClicked,
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
    ) {
        androidx.compose.foundation.layout.Column(
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
            )

            Spacer(Modifier.height(dimens.space3))

            TermsCheckboxText(
                modifier = Modifier.fillMaxWidth(),
                accepted = state.termsAccepted,
                onToggle = intent::onTermsToggled,
                onPrivacyPolicyClick = intent::onPrivacyPolicyClicked,
                onTermsOfServiceClick = intent::onTermsOfServiceClicked,
            )

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
                onClick = launchGoogleSignIn,
                enabled = !state.isLoading,
                loading = state.isGoogleLoading,
            )

            Spacer(Modifier.height(18.dp))

            AuthAltRow(
                question = stringResource(R.string.sign_up_alt_q),
                action = stringResource(R.string.sign_up_alt_cta),
                onActionClick = intent::onSignInClicked,
            )
        }
    }
}
