package com.kovhan.core.models.feedback

enum class FeedbackSource {
    GENERAL,
    WIDGET,
}

data class Feedback(
    val liked: Boolean,
    val comment: String,
    val source: FeedbackSource,
)
