package com.kovhan.core.analytics

interface AnalyticsValue {
    val value: String
}

enum class PaywallSource(override val value: String) : AnalyticsValue {
    HOME("home"),
    LIMIT("limit"),
    BANNER("banner"),
    TENURE("30_days"),
    CANCEL("cancel"),
}

enum class PaywallType(override val value: String) : AnalyticsValue {
    SUBSCRIPTION("Subscription"),
    SPECIAL_OFFER("special offer"),
}

enum class PurchaseFailure(override val value: String) : AnalyticsValue {
    BILLING_ERROR("billing_error"),
    NETWORK_ERROR("network_error"),
    PRODUCT_UNAVAILABLE("product_unavailable"),
    PAYMENT_DECLINED("payment_declined"),
    VERIFICATION_FAILED("verification_failed"),
    ALREADY_OWNED("already_owned"),
    UNKNOWN("unknown"),
}

enum class LimitReason(override val value: String) : AnalyticsValue {
    COLLECTION("collection_4"),
    QUOTES("quotes_11"),
    EDIT_COLLECTION("edit_collection"),
    AI_TAGS("ai_tags"),
    AI_CAMERA("ai_camera"),
}

enum class RestoreStatus(override val value: String) : AnalyticsValue {
    SUCCESS("success"),
    FAILED("failed"),
}

enum class SubscriptionState(override val value: String) : AnalyticsValue {
    FREE("free"),
    PREMIUM("Premium"),
}

enum class AuthEntry(override val value: String) : AnalyticsValue {
    FIRST_LAUNCH("first_launch"),
    SETTINGS("settings"),
}

enum class AuthProvider(override val value: String) : AnalyticsValue {
    EMAIL("email"),
    GOOGLE("google"),
}

enum class AuthType(override val value: String) : AnalyticsValue {
    ANONYMOUS("anonymous"),
    EMAIL("email"),
    GOOGLE("google"),
}

enum class WelcomeResult(override val value: String) : AnalyticsValue {
    CREATE("create"),
    HAVE("have"),
    LATER("later"),
}

enum class AccountDeleteResult(override val value: String) : AnalyticsValue {
    DELETED("deleted"),
    CANCEL("cancel"),
}

enum class CollectionCreateSource(override val value: String) : AnalyticsValue {
    LIBRARY("library"),
    ADD_QUOTE("add_quote"),
}

enum class CollectionAction(override val value: String) : AnalyticsValue {
    EDIT("edit"),
    RENAME("rename"),
    DELETE("delete"),
}

enum class QuoteAction(override val value: String) : AnalyticsValue {
    EDIT("edit"),
    MOVE("move"),
    DELETE("delete"),
}

enum class DailyQuoteResult(override val value: String) : AnalyticsValue {
    REMOVE("remove"),
    HIDE_FOR_TODAY("hide_for_today"),
    KEEP("keep"),
}

enum class InputMethod(override val value: String) : AnalyticsValue {
    TEXT("text"),
    CAMERA("camera"),
    VOICE("voice"),
}

enum class AddQuoteSource(override val value: String) : AnalyticsValue {
    TAB("tab"),
    EMPTY_COLLECTION("empty_collection"),
}

enum class AddQuoteStep(override val value: String) : AnalyticsValue {
    QUOTE_INPUT("quote_input"),
    ADDITIONAL_INFO("additional_info"),
}

enum class PermissionType(override val value: String) : AnalyticsValue {
    CAMERA("camera"),
    MICROPHONE("microphone"),
    NOTIFICATIONS("notifications"),
    PHOTOS("photos"),
}

enum class PermissionResult(override val value: String) : AnalyticsValue {
    GRANTED("granted"),
    DENIED("denied"),
    PERMANENTLY_DENIED("permanently_denied"),
}

enum class CameraFailure(override val value: String) : AnalyticsValue {
    NO_TEXT_DETECTED("no_text_detected"),
    PERMISSION_DENIED("camera_permission_denied"),
    IMAGE_LOAD_FAILED("image_load_failed"),
    RECOGNITION_FAILED("recognition_failed"),
    CAMERA_ERROR("camera_error"),
    UNKNOWN("unknown"),
}

enum class VoiceFailure(override val value: String) : AnalyticsValue {
    PERMISSION_DENIED("permission_denied"),
    NO_SPEECH_DETECTED("no_speech_detected"),
    RECOGNITION_FAILED("recognition_failed"),
    NETWORK_ERROR("network_error"),
    CANCELLED("cancelled"),
    UNKNOWN("unknown"),
}

enum class AiFeatureName(override val value: String) : AnalyticsValue {
    CAMERA("camera"),
    TAGS("tags"),
}

enum class FeedbackReason(override val value: String) : AnalyticsValue {
    WIDGET("widget"),
    FIRST_QUOTE("first_quote"),
    SETTINGS("settings"),
}

enum class FeedbackResult(override val value: String) : AnalyticsValue {
    LIKE("like"),
    DISLIKE("dislike"),
}

enum class WidgetStyleName(override val value: String) : AnalyticsValue {
    MINIMAL("minimal"),
    CLASSIC("clasic"),
    COVER("cover"),
}

enum class WidgetQuoteSource(override val value: String) : AnalyticsValue {
    ALL("all"),
    FAVOURITES("favourites"),
    CUSTOM("custom"),
}

enum class WidgetTextColour(override val value: String) : AnalyticsValue {
    WHITE("white"),
    BLACK("black"),
    CUSTOM("custom"),
}

enum class WidgetTextAlignment(override val value: String) : AnalyticsValue {
    LEFT("left"),
    CENTER("center"),
}

enum class AppTheme(override val value: String) : AnalyticsValue {
    DARK("dark"),
    LIGHT("light"),
}
