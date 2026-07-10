package com.kovhan.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
data object SearchKey : NavKey

/** Kind of entity whose quotes the universal details screen can list. */
@Serializable
enum class EntityType { COLLECTION, TAG, BOOK, AUTHOR }

/**
 * Read-only details of a derived entity (tag / book / author) — reuses the
 * collection-details chrome to list all quotes of that entity.
 */
@Serializable
data class EntityDetailsKey(
    val type: EntityType,
    val entityId: String,
    val title: String = "",
) : NavKey

@Serializable
data object FavoritesKey : NavKey

@Serializable
data object ProfileKey : NavKey

@Serializable
data object EditProfileKey : NavKey

@Serializable
data object AboutKey : NavKey

@Serializable
data class AddQuoteKey(val tab: AddQuoteTab = AddQuoteTab.TEXT) : NavKey

/**
 * "Additional info" step of the add-quote flow. Receives the captured quote
 * text and lets the user attach an author, book, tags and playlist toggles
 * before saving.
 */
@Serializable
data class QuoteDetailsKey(val quote: String) : NavKey

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

@Serializable
data class TagSheetKey(
    val quoteText: String,
    val selectedTags: List<String>,
    val tagPool: List<String>,
    val aiTags: List<String>,
) : BottomSheetKey

@Serializable
data class SaveQuoteCollectionSheetKey(
    val text: String,
    val authorName: String?,
    val bookName: String?,
    val tagNames: List<String>,
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
) : BottomSheetKey

@Serializable
data class NewCollectionSheetKey(
    val text: String,
    val authorName: String?,
    val bookName: String?,
    val tagNames: List<String>,
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
) : BottomSheetKey

@Serializable
data class RenameEntitySheetKey(
    val type: EntityType,
    val initialName: String,
) : BottomSheetKey

@Serializable
data class EditCollectionStyleSheetKey(
    val iconId: String,
    val tone: String,
) : BottomSheetKey

@Serializable
data class EditQuoteSheetKey(
    val quoteId: String,
    val text: String,
    val authorName: String,
    val bookName: String,
    val tags: List<String>,
    val aiTags: List<String>,
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
    val authorOptions: List<String>,
    val bookOptions: List<String>,
    val tagPool: List<String>,
) : BottomSheetKey

@Serializable
data class MoveQuoteSheetKey(
    val quoteId: String,
    val selectedCollectionId: String?,
    val excludedCollectionId: String?,
) : BottomSheetKey

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

@Serializable
data class TagSheetAiLimitDialogKey(val reason: AiLimitDialogReason) : DialogKey

/**
 * Generic confirmation dialog. The caller supplies the display strings (as
 * resource ids) and the [resultKey] to emit on confirm. When [payload] is set
 * it is echoed back as the result; otherwise the result is simply `true`.
 */
@Serializable
data class ConfirmDialogKey(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int,
    @StringRes val confirmRes: Int,
    @StringRes val cancelRes: Int,
    val resultKey: String,
    val payload: String? = null,
) : DialogKey

/** Blocking startup dialog shown when the device is offline and has no subscription. */
@Serializable
data object OfflineBlockingDialogKey : DialogKey

/** Small "you're offline, this AI feature needs internet" dialog for the tag editor. */
@Serializable
data object TagSheetOfflineDialogKey : DialogKey

// ---------------------------------------------------------------------------
// OVERLAY PAYLOADS
// ---------------------------------------------------------------------------

@Serializable
enum class EditField { NAME, USERNAME, EMAIL }

@Serializable
enum class AddQuoteTab { TEXT, SCAN, VOICE }

@Serializable
enum class AiLimitDialogReason {
    NOT_REGISTERED,
    FREE_LIMIT_REACHED,
    DAILY_LIMIT_REACHED,
    MONTHLY_LIMIT_REACHED,
}

@Serializable
enum class QuoteRemovalMode {
    DELETE,
    REMOVE_FROM_COLLECTION,
    REMOVE_FROM_ENTITY,
}

enum class PhotoAction { TAKE, GALLERY, REMOVE }
