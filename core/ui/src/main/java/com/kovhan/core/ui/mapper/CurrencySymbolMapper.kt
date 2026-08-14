package com.kovhan.core.ui.mapper

import java.util.Currency
import java.util.Locale

object CurrencySymbolMapper {

    private val symbols = mapOf(
        "UAH" to "₴",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "PLN" to "zł",
        "CZK" to "Kč",
        "KZT" to "₸",
        "TRY" to "₺",
        "JPY" to "¥",
        "CNY" to "¥",
        "INR" to "₹",
        "BRL" to "R$",
        "CHF" to "CHF",
        "SEK" to "kr",
        "NOK" to "kr",
        "DKK" to "kr",
        "CAD" to "$",
        "AUD" to "$",
    )

    fun symbolOf(currencyCode: String, locale: Locale = Locale.getDefault()): String {
        val code = currencyCode.uppercase(Locale.ROOT)
        symbols[code]?.let { return it }
        return runCatching { Currency.getInstance(code).getSymbol(locale) }.getOrNull() ?: code
    }
}
