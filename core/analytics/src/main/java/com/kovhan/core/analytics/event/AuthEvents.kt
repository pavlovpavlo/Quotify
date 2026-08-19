package com.kovhan.core.analytics.event

import com.kovhan.core.analytics.AccountDeleteResult
import com.kovhan.core.analytics.AnalyticsEvent
import com.kovhan.core.analytics.AnalyticsParam
import com.kovhan.core.analytics.AuthEntry
import com.kovhan.core.analytics.AuthProvider
import com.kovhan.core.analytics.WelcomeResult

data object AppStart : AnalyticsEvent(name = "app_start")

data object OnboardingStarted : AnalyticsEvent(name = "onboarding_started")

class OnboardingStep(step: Int) : AnalyticsEvent(
    name = "onboarding_step",
    params = mapOf(AnalyticsParam.STEP to step.toString()),
)

class OnboardingSkip(step: Int) : AnalyticsEvent(
    name = "onboarding_skip",
    params = mapOf(AnalyticsParam.STEP to step.toString()),
)

data object OnboardingFinished : AnalyticsEvent(name = "onboarding_finished")

class WelcomeScreenResult(result: WelcomeResult) : AnalyticsEvent(
    name = "welcome_screen_result",
    params = mapOf(AnalyticsParam.RESULT to result.value),
)

class SignInOpened(entry: AuthEntry) : AnalyticsEvent(
    name = "sign_in_opened",
    params = mapOf(AnalyticsParam.REASON to entry.value),
)

class SignInFailed(entry: AuthEntry, failure: String) : AnalyticsEvent(
    name = "sign_in_failed",
    params = mapOf(
        AnalyticsParam.REASON to entry.value,
        AnalyticsParam.FAILURE to failure,
    ),
)

class SignInFinished(entry: AuthEntry, provider: AuthProvider) : AnalyticsEvent(
    name = "sign_in_finished",
    params = mapOf(
        AnalyticsParam.REASON to entry.value,
        AnalyticsParam.RESULT to provider.value,
    ),
)

class SignUpOpened(entry: AuthEntry) : AnalyticsEvent(
    name = "sign_up_opened",
    params = mapOf(AnalyticsParam.REASON to entry.value),
)

class SignUpFailed(entry: AuthEntry, failure: String) : AnalyticsEvent(
    name = "sign_up_failed",
    params = mapOf(
        AnalyticsParam.REASON to entry.value,
        AnalyticsParam.FAILURE to failure,
    ),
)

class SignUpFinished(entry: AuthEntry, provider: AuthProvider) : AnalyticsEvent(
    name = "sign_up_finished",
    params = mapOf(
        AnalyticsParam.REASON to entry.value,
        AnalyticsParam.RESULT to provider.value,
    ),
)

data object PasswordResetInitiated : AnalyticsEvent(name = "password_reset_initiated")

data object PasswordResetFinished : AnalyticsEvent(name = "password_reset_finished")

data object LogoutFinished : AnalyticsEvent(name = "logout_finished")

class AccountDeleteFinished(result: AccountDeleteResult) : AnalyticsEvent(
    name = "account_delete_finished",
    params = mapOf(AnalyticsParam.RESULT to result.value),
)
