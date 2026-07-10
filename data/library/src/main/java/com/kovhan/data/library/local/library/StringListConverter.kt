package com.kovhan.data.library.local.library

import androidx.room.TypeConverter

class StringListConverter {

    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(SEPARATOR)

    @TypeConverter
    fun toList(value: String): List<String> =
        if (value.isEmpty()) emptyList() else value.split(SEPARATOR)

    private companion object {
        // ASCII unit separator (0x1F): safe because entity ids are UUIDs.
        val SEPARATOR = Char(31).toString()
    }
}
