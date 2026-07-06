package com.kovhan.core.ui.mapper

import androidx.annotation.DrawableRes
import com.kovhan.design.systems.R


object CollectionIconMapper {

    const val DEFAULT_ICON_ID = "bookmark"

    private val icons: Map<String, Int> = mapOf(
        "default" to R.drawable.ic_collection_default,
        "anchor" to R.drawable.ic_collection_anchor,
        "bell" to R.drawable.ic_collection_bell,
        "book" to R.drawable.ic_collection_book,
        "bookmark" to R.drawable.ic_collection_bookmark,
        "bulb" to R.drawable.ic_collection_bulb,
        "camera" to R.drawable.ic_collection_camera,
        "cloud" to R.drawable.ic_collection_cloud,
        "coffee" to R.drawable.ic_collection_coffee,
        "compass" to R.drawable.ic_collection_compass,
        "crown" to R.drawable.ic_collection_crown,
        "feather" to R.drawable.ic_collection_feather,
        "flower" to R.drawable.ic_collection_flower,
        "gem" to R.drawable.ic_collection_gem,
        "gift" to R.drawable.ic_collection_gift,
        "globe" to R.drawable.ic_collection_globe,
        "heart" to R.drawable.ic_collection_heart,
        "key" to R.drawable.ic_collection_key,
        "leaf" to R.drawable.ic_collection_leaf,
        "moon" to R.drawable.ic_collection_moon,
        "mountain" to R.drawable.ic_collection_mountain,
        "music" to R.drawable.ic_collection_music,
        "paw" to R.drawable.ic_collection_paw,
        "pen" to R.drawable.ic_collection_pen,
        "quote" to R.drawable.ic_collection_quote,
        "rocket" to R.drawable.ic_collection_rocket,
        "smile" to R.drawable.ic_collection_smile,
        "sparkles" to R.drawable.ic_collection_sparkles,
        "star" to R.drawable.ic_collection_star,
        "sun" to R.drawable.ic_collection_sun,
        "umbrella" to R.drawable.ic_collection_umbrella,
    )

    val iconIds: List<String> = icons.keys.toList()

    @DrawableRes
    fun toDrawableRes(iconId: String): Int =
        icons[iconId.lowercase()] ?: icons.getValue(DEFAULT_ICON_ID)
}
