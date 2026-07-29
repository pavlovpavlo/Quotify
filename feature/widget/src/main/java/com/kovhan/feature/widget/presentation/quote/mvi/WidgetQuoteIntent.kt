package com.kovhan.feature.widget.presentation.quote.mvi

interface WidgetQuoteIntent {
    fun onToggleMenu()
    fun onDismissMenu()
    fun onEditClicked()
    fun onDeleteClicked()

    companion object {
        val Empty: WidgetQuoteIntent = object : WidgetQuoteIntent {
            override fun onToggleMenu() = Unit
            override fun onDismissMenu() = Unit
            override fun onEditClicked() = Unit
            override fun onDeleteClicked() = Unit
        }
    }
}
