package com.kovhan.feature.main.presentation.about.model

import androidx.annotation.StringRes
import com.kovhan.design.systems.R

internal sealed interface AboutBlock {
    data class Paragraph(@StringRes val text: Int) : AboutBlock

    data class Bullets(val items: List<Int>) : AboutBlock
}

internal data class AboutSection(
    @StringRes val title: Int?,
    val blocks: List<AboutBlock>,
)

internal val aboutSections: List<AboutSection> = listOf(
    AboutSection(
        title = null,
        blocks = listOf(
            AboutBlock.Paragraph(R.string.about_intro_1),
            AboutBlock.Paragraph(R.string.about_intro_2),
            AboutBlock.Paragraph(R.string.about_intro_3),
        ),
    ),
    AboutSection(
        title = R.string.about_save_title,
        blocks = listOf(
            AboutBlock.Bullets(
                listOf(
                    R.string.about_save_1,
                    R.string.about_save_2,
                    R.string.about_save_3,
                ),
            ),
            AboutBlock.Paragraph(R.string.about_save_note),
        ),
    ),
    AboutSection(
        title = R.string.about_organize_title,
        blocks = listOf(
            AboutBlock.Bullets(
                listOf(
                    R.string.about_organize_1,
                    R.string.about_organize_2,
                    R.string.about_organize_3,
                    R.string.about_organize_4,
                ),
            ),
            AboutBlock.Paragraph(R.string.about_organize_note),
        ),
    ),
    AboutSection(
        title = R.string.about_daily_title,
        blocks = listOf(
            AboutBlock.Paragraph(R.string.about_daily_1),
            AboutBlock.Paragraph(R.string.about_daily_2),
        ),
    ),
    AboutSection(
        title = R.string.about_offline_title,
        blocks = listOf(
            AboutBlock.Paragraph(R.string.about_offline_1),
            AboutBlock.Paragraph(R.string.about_offline_2),
        ),
    ),
    AboutSection(
        title = R.string.about_premium_title,
        blocks = listOf(
            AboutBlock.Paragraph(R.string.about_premium_intro),
            AboutBlock.Bullets(
                listOf(
                    R.string.about_premium_1,
                    R.string.about_premium_2,
                    R.string.about_premium_3,
                ),
            ),
            AboutBlock.Paragraph(R.string.about_premium_billing),
        ),
    ),
    AboutSection(
        title = R.string.about_guest_title,
        blocks = listOf(
            AboutBlock.Paragraph(R.string.about_guest_1),
            AboutBlock.Paragraph(R.string.about_guest_2),
        ),
    ),
    AboutSection(
        title = null,
        blocks = listOf(
            AboutBlock.Paragraph(R.string.about_closing_1),
            AboutBlock.Paragraph(R.string.about_closing_2),
        ),
    ),
)
