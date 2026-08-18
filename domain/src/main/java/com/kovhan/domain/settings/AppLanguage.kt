package com.kovhan.domain.settings

import android.content.res.Resources

enum class AppLanguage(val tag: String) {
    UKRAINIAN("uk"),
    ENGLISH("en");

    companion object {

        val default: AppLanguage
            get() {
                val locales = Resources.getSystem().configuration.locales
                if (locales.isEmpty) return ENGLISH
                val language = locales[0].language
                return entries.firstOrNull { it.tag.equals(language, ignoreCase = true) } ?: ENGLISH
            }

        fun fromTag(tag: String?): AppLanguage =
            entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) } ?: default
    }
}
