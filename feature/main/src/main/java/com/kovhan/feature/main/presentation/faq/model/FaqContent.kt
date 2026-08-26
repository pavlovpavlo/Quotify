package com.kovhan.feature.main.presentation.faq.model

import androidx.annotation.StringRes
import com.kovhan.design.systems.R

internal data class FaqEntry(
    @StringRes val question: Int,
    @StringRes val answer: Int,
)

internal data class FaqSection(
    @StringRes val title: Int,
    val entries: List<FaqEntry>,
)

internal val faqSections: List<FaqSection> = listOf(
    FaqSection(
        title = R.string.faq_section_general,
        entries = listOf(
            FaqEntry(R.string.faq_q_what_is, R.string.faq_a_what_is),
            FaqEntry(R.string.faq_q_offline, R.string.faq_a_offline),
            FaqEntry(R.string.faq_q_sync, R.string.faq_a_sync),
        ),
    ),
    FaqSection(
        title = R.string.faq_section_features,
        entries = listOf(
            FaqEntry(R.string.faq_q_add_quote, R.string.faq_a_add_quote),
            FaqEntry(R.string.faq_q_scan, R.string.faq_a_scan),
            FaqEntry(R.string.faq_q_folders_tags, R.string.faq_a_folders_tags),
            FaqEntry(R.string.faq_q_favourites, R.string.faq_a_favourites),
            FaqEntry(R.string.faq_q_daily, R.string.faq_a_daily),
            FaqEntry(R.string.faq_q_widget, R.string.faq_a_widget),
            FaqEntry(R.string.faq_q_appearance, R.string.faq_a_appearance),
        ),
    ),
    FaqSection(
        title = R.string.faq_section_subscription,
        entries = listOf(
            FaqEntry(R.string.faq_q_premium, R.string.faq_a_premium),
            FaqEntry(R.string.faq_q_free_limits, R.string.faq_a_free_limits),
            FaqEntry(R.string.faq_q_cancel, R.string.faq_a_cancel),
            FaqEntry(R.string.faq_q_not_active, R.string.faq_a_not_active),
            FaqEntry(R.string.faq_q_expired, R.string.faq_a_expired),
        ),
    ),
    FaqSection(
        title = R.string.faq_section_account,
        entries = listOf(
            FaqEntry(R.string.faq_q_why_account, R.string.faq_a_why_account),
            FaqEntry(R.string.faq_q_reset_email, R.string.faq_a_reset_email),
            FaqEntry(R.string.faq_q_delete_account, R.string.faq_a_delete_account),
        ),
    ),
)
