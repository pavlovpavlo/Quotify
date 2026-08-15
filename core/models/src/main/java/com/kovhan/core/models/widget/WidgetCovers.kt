package com.kovhan.core.models.widget

/**
 * Ordered cover ids for the cover style. Each id maps to a bundled photo in
 * `core:ui`, together with the border colour sampled from it.
 */
object WidgetCovers {

    private const val COUNT = 15

    val ALL: List<String> = List(COUNT) { index -> "cover_${index + 1}" }

    val DEFAULT: String = ALL.first()
}
