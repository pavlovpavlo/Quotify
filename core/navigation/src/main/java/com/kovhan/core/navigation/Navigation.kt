package com.kovhan.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.core.navigation.models.AddQuoteEntryPoint
import com.kovhan.core.navigation.models.AuthEntryPoint
import com.kovhan.core.navigation.models.PaywallOrigin
import com.kovhan.core.navigation.models.QuoteInputMethod
import com.kovhan.domain.settings.AppLanguage
import com.kovhan.domain.settings.AppTheme
import kotlinx.serialization.Serializable

interface BottomSheetKey : NavKey

interface DialogKey : NavKey

@Serializable
data object SplashKey : NavKey

@Serializable
data object OnboardingKey : NavKey

@Serializable
data object CompleteKey : NavKey

@Serializable
data class LoginKey(
    val confirmDelete: Boolean = false,
    val entryPoint: AuthEntryPoint = AuthEntryPoint.FIRST_LAUNCH,
) : NavKey

@Serializable
data class RegisterKey(
    val entryPoint: AuthEntryPoint = AuthEntryPoint.FIRST_LAUNCH,
) : NavKey

@Serializable
data object ForgotPasswordKey : NavKey

@Serializable
data object HomeKey : NavKey

@Serializable
data object QuotesKey : NavKey

@Serializable
data object SearchKey : NavKey

@Serializable
enum class EntityType { COLLECTION, TAG, BOOK, AUTHOR }

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
data class PaywallKey(val origin: PaywallOrigin = PaywallOrigin.HOME) : NavKey

@Serializable
data object SubscriptionKey : NavKey

@Serializable
data class OfferKey(val origin: PaywallOrigin = PaywallOrigin.BANNER) : NavKey

@Serializable
data object DevToolsKey : NavKey

@Serializable
data class SurveyKey(val surveyId: String = DEFAULT_SURVEY_ID) : NavKey

@Serializable
data object SurveyPlatePreviewKey : NavKey

const val DEFAULT_SURVEY_ID = "quotify_v1"

const val EXTRA_OPEN_WIDGET_SETTINGS = "com.kovhan.quotify.OPEN_WIDGET_SETTINGS"
const val EXTRA_OPEN_WIDGET_QUOTE = "com.kovhan.quotify.OPEN_WIDGET_QUOTE"

@Serializable
data object WidgetSettingsKey : NavKey

@Serializable
data class WidgetQuoteKey(val quoteId: String) : NavKey

@Serializable
data class PlaylistPickerKey(val playlistId: String? = null) : NavKey

@Serializable
data class WidgetAppearanceKey(val style: String) : NavKey

@Serializable
data class AddQuoteKey(
    val tab: AddQuoteTab = AddQuoteTab.TEXT,
    val entryPoint: AddQuoteEntryPoint = AddQuoteEntryPoint.TAB,
) : NavKey

@Serializable
data class QuoteDetailsKey(
    val quote: String,
    val inputMethod: QuoteInputMethod = QuoteInputMethod.TEXT,
) : NavKey

@Serializable
data class WebViewKey(val title: String, val url: String) : NavKey

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
    val inputMethod: QuoteInputMethod = QuoteInputMethod.TEXT,
    val text: String,
    val authorName: String?,
    val bookName: String?,
    val tagNames: List<String>,
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
    val page: Int? = null,
) : BottomSheetKey

@Serializable
data class NewCollectionSheetKey(
    val inputMethod: QuoteInputMethod = QuoteInputMethod.TEXT,
    val text: String,
    val authorName: String?,
    val bookName: String?,
    val tagNames: List<String>,
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
    val page: Int? = null,
) : BottomSheetKey

@Serializable
data class RenameEntitySheetKey(
    val type: EntityType,
    val initialName: String,
) : BottomSheetKey

@Serializable
data class WidgetFrequencySheetKey(val hours: Int) : BottomSheetKey

@Serializable
data class PlaylistNameSheetKey(
    val mode: PlaylistNameMode,
    val initialName: String,
) : BottomSheetKey

@Serializable
data class EditCollectionStyleSheetKey(
    val iconId: String,
    val tone: String,
) : BottomSheetKey

@Serializable
data object ManageSubscriptionSheetKey : BottomSheetKey

@Serializable
data class EditQuoteKey(
    val quoteId: String,
    val text: String,
    val authorName: String,
    val bookName: String,
    val tags: List<String>,
    val aiTags: List<String>,
    val inWidgetPlaylist: Boolean,
    val inPushPlaylist: Boolean,
    val page: String,
    val authorOptions: List<String>,
    val bookOptions: List<String>,
    val tagPool: List<String>,
) : NavKey

@Serializable
data class MoveQuoteSheetKey(
    val quoteId: String,
    val selectedCollectionId: String?,
    val excludedCollectionId: String?,
    val keepsFavourite: Boolean = false,
) : BottomSheetKey

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

@Serializable
data class ConfirmDialogKey(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int,
    @StringRes val confirmRes: Int,
    @StringRes val cancelRes: Int?,
    val resultKey: String,
    val payload: String? = null,
) : DialogKey

@Serializable
data object WidgetPromoKey: DialogKey

@Serializable
data class SurveyInviteDialogKey(val surveyId: String = DEFAULT_SURVEY_ID) : DialogKey

@Serializable
enum class SurveyInviteAction { START, LATER, DISMISS }

@Serializable
data class SurveyExitDialogKey(val surveyId: String = DEFAULT_SURVEY_ID) : DialogKey

@Serializable
enum class SurveyExitAction { LATER, SKIP }

@Serializable
data object WidgetSettingsExitDialogKey : DialogKey

@Serializable
enum class WidgetExitAction { APPLY, LEAVE }

@Serializable
data class FeedbackDialogKey(
    val source: FeedbackSource = FeedbackSource.GENERAL,
    val liked: Boolean? = null,
) : DialogKey
@Serializable
data object OfflineBlockingSheetKey : BottomSheetKey

@Serializable
data object UpdateRequiredDialogKey : DialogKey

@Serializable
data object TagSheetOfflineDialogKey : DialogKey

@Serializable
enum class EditField { NAME, USERNAME, EMAIL }

@Serializable
enum class AddQuoteTab { TEXT, SCAN, VOICE }

@Serializable
enum class PlaylistNameMode { CREATE, RENAME }

@Serializable
enum class AiLimitDialogReason {
    NOT_REGISTERED,
    SUBSCRIPTION_REQUIRED,
    FREE_DAILY_LIMIT_REACHED,
    FREE_MONTHLY_LIMIT_REACHED,
    DAILY_LIMIT_REACHED,
    MONTHLY_LIMIT_REACHED,
}

@Serializable
enum class QuoteRemovalMode {
    DELETE,
    REMOVE_FROM_COLLECTION,
    REMOVE_FROM_ENTITY,
    REMOVE_FROM_FAVOURITES,
}

enum class PhotoAction { TAKE, GALLERY, REMOVE }
