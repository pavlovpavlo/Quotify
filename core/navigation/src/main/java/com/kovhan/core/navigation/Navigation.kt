package com.kovhan.core.navigation

import androidx.navigation3.runtime.NavKey
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import kotlinx.serialization.Serializable

/** Marker for keys rendered in the bottom-sheet stack. */
interface BottomSheetKey : NavKey

/** Marker for keys rendered in the dialog stack. */
interface DialogKey : NavKey

// ---------------------------------------------------------------------------
// SCREEN KEYS
// ---------------------------------------------------------------------------

@Serializable
data object SplashKey : NavKey

@Serializable
data object OnboardingKey : NavKey

/**
 * "You're all set" screen shown once after onboarding, before the user picks
 * sign-in vs sign-up.
 */
@Serializable
data object CompleteKey : NavKey

@Serializable
data object LoginKey : NavKey

@Serializable
data object RegisterKey : NavKey

@Serializable
data object ForgotPasswordKey : NavKey

@Serializable
data object HomeKey : NavKey

@Serializable
data object QuotesKey : NavKey

@Serializable
data object FavoritesKey : NavKey

@Serializable
data object ProfileKey : NavKey

@Serializable
data object EditProfileKey : NavKey

@Serializable
data object AboutKey : NavKey

/**
 * Generic external-page key. Any feature can navigate here for Privacy Policy,
 * Terms of Service, blog posts, etc.
 */
@Serializable
data class WebViewKey(val title: String, val url: String) : NavKey

// ---------------------------------------------------------------------------
// BOTTOM SHEET KEYS
// ---------------------------------------------------------------------------

@Serializable
data object PlaceholderBottomSheetKey : BottomSheetKey

@Serializable
data class ThemeSheetKey(val selected: AppTheme) : BottomSheetKey

@Serializable
data class LanguageSheetKey(val selected: AppLanguage) : BottomSheetKey

@Serializable
data class ReminderTimeSheetKey(val hour: Int, val minute: Int) : BottomSheetKey

@Serializable
data object ChangePhotoSheetKey : BottomSheetKey

@Serializable
data class EditFieldSheetKey(val field: EditField, val initialValue: String) : BottomSheetKey

// ---------------------------------------------------------------------------
// DIALOG KEYS
// ---------------------------------------------------------------------------

@Serializable
data object PlaceholderDialogKey : DialogKey

@Serializable
data object LogoutDialogKey : DialogKey

@Serializable
data object DeleteAccountDialogKey : DialogKey

@Serializable
data object HideDailyQuoteDialogKey : DialogKey

// ---------------------------------------------------------------------------
// OVERLAY PAYLOADS
// ---------------------------------------------------------------------------

@Serializable
enum class EditField { NAME, USERNAME, EMAIL }

enum class PhotoAction { TAKE, GALLERY, REMOVE }
