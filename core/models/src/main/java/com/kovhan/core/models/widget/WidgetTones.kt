package com.kovhan.core.models.widget

/**
 * Ordered background tone tokens for the classic style. Tokens rather than
 * colours so the model stays platform-free — `core:ui` resolves each id to its
 * fill, border shade and readable text colour.
 */
object WidgetTones {

    val ALL: List<String> = listOf(
        "papier",
        "terra",
        "ai",
        "olive",
        "gold",
        "ink",
        "plum",
        "teal",
        "sand",
        "blush",
        "rust",
        "forest",
        "slate",
        "clay",
        "wine",
    )

    val DEFAULT: String = ALL.first()
}
