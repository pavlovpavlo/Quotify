package com.kovhan.feature.main.presentation.devtools

import com.kovhan.core.models.feedback.FeedbackSource
import com.kovhan.core.navigation.AiLimitDialogReason
import com.kovhan.core.navigation.ConfirmDialogKey
import com.kovhan.core.navigation.DeleteAccountDialogKey
import com.kovhan.core.navigation.DialogKey
import com.kovhan.core.navigation.FeedbackDialogKey
import com.kovhan.core.navigation.HideDailyQuoteDialogKey
import com.kovhan.core.navigation.LogoutDialogKey
import com.kovhan.core.navigation.TagSheetAiLimitDialogKey
import com.kovhan.core.navigation.TagSheetOfflineDialogKey
import com.kovhan.core.navigation.WidgetPromoKey
import com.kovhan.design.systems.R

internal const val DEV_TOOLS_CONFIRM_RESULT_KEY = "dev_tools_confirm"

internal data class DevDialogSample(
    val label: String,
    val key: DialogKey,
)

internal val DevDialogCatalog: List<DevDialogSample> = buildList {
    add(DevDialogSample("LogoutDialogKey", LogoutDialogKey))
    add(DevDialogSample("DeleteAccountDialogKey", DeleteAccountDialogKey))
    add(DevDialogSample("HideDailyQuoteDialogKey", HideDailyQuoteDialogKey))
    add(DevDialogSample("TagSheetOfflineDialogKey", TagSheetOfflineDialogKey))
    add(DevDialogSample("WidgetPromoKey", WidgetPromoKey))
    add(DevDialogSample("FeedbackDialogKey", FeedbackDialogKey()))
    add(
        DevDialogSample(
            label = "FeedbackDialogKey · WIDGET 👍",
            key = FeedbackDialogKey(source = FeedbackSource.WIDGET, liked = true),
        ),
    )
    add(
        DevDialogSample(
            label = "FeedbackDialogKey · WIDGET 👎",
            key = FeedbackDialogKey(source = FeedbackSource.WIDGET, liked = false),
        ),
    )
    add(
        DevDialogSample(
            label = "ConfirmDialogKey",
            key = ConfirmDialogKey(
                iconRes = R.drawable.ic_trash,
                titleRes = R.string.collection_details_delete_quote_title,
                messageRes = R.string.entity_delete_quote_message,
                confirmRes = R.string.collection_details_delete_quote_confirm,
                cancelRes = R.string.dialog_cancel,
                resultKey = DEV_TOOLS_CONFIRM_RESULT_KEY,
            ),
        ),
    )
    AiLimitDialogReason.entries.forEach { reason ->
        add(
            DevDialogSample(
                label = "TagSheetAiLimitDialogKey · ${reason.name}",
                key = TagSheetAiLimitDialogKey(reason),
            ),
        )
    }
}
