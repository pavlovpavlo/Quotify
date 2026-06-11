package com.kovhan.core.navigation

import androidx.navigation3.runtime.NavKey

enum class TabEnum(val key: NavKey) {
    LIBRARY(QuotesKey),
    PROFILE(ProfileKey);

    companion object {
        fun fromKey(key: NavKey?): TabEnum? = when (key) {
            QuotesKey -> LIBRARY
            ProfileKey -> PROFILE
            else -> null
        }
    }
}
