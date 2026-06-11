package com.kovhan.domain.settings

enum class AppLanguage(val tag: String) {
    UKRAINIAN("uk"),
    ENGLISH("en");

    companion object {
        fun fromTag(tag: String?): AppLanguage =
            entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) } ?: UKRAINIAN
    }
}
