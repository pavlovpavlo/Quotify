package com.kovhan.feature.auth.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.kovhan.feature.auth.presentation.component.AuthTextLink
import com.kovhan.feature.auth.presentation.component.AuthTitleBlock
import com.kovhan.feature.auth.presentation.component.GoogleAuthButton
import com.kovhan.feature.auth.presentation.component.LegalFooter
import com.kovhan.feature.auth.presentation.google.rememberGoogleSignIn
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenIntent
import com.kovhan.feature.auth.presentation.login.mvi.LoginScreenState
import com.kovhan.feature.auth.presentation.login.navigation.LoginScreenNavAction

@Composable
fun LoginScreen(
    state: LoginScreenState,
    intent: LoginScreenIntent,
    navAction: LoginScreenNavAction,
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
        AuthTitleBlock(
            title = stringResource(R.string.sign_in_title),
            subtitle = stringResource(R.string.sign_in_subtitle),
        )

        Spacer(Modifier.height(18.dp))

        QuotifyTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            label = stringResource(R.string.auth_email),
            placeholder = stringResource(R.string.sign_in_email_placeholder),
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
            placeholder = stringResource(R.string.sign_in_password_placeholder),
            imeAction = ImeAction.Done,
            onValueChange = intent::onPasswordChanged,
        )

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            AuthTextLink(
                text = stringResource(R.string.sign_in_forgot),
                onClick = intent::onForgotPasswordClicked,
                fontSize = 12.sp,
                fontWeight = FontWeight.W500,
            )
        }

        Spacer(Modifier.height(dimens.space4))

        QuotifyButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.sign_in_cta),
            onClick = intent::onSignInClicked,
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

        Spacer(Modifier.height(28.dp))

        LegalFooter(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .align(Alignment.CenterHorizontally),
            onTermsClick = intent::onTermsOfServiceClicked,
            onPrivacyClick = intent::onPrivacyPolicyClicked,
        )

        Spacer(Modifier.height(10.dp))

        AuthAltRow(
            question = stringResource(R.string.sign_in_alt_q),
            action = stringResource(R.string.sign_in_alt_cta),
            onActionClick = intent::onSignUpClicked,
        )
    }
}
