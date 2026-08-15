package com.kovhan.feature.common.component.dialog

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.kovhan.core.navigation.FeedbackDialogKey
import com.kovhan.core.navigation.NavigationCoordinator

@Composable
fun FeedbackDialogEntry(
    key: FeedbackDialogKey,
    coordinator: NavigationCoordinator,
) {
    val viewModel = hiltViewModel<FeedbackViewModel>()

    FeedbackDialog(
        initialLiked = key.liked,
        onSubmit = { liked, comment -> viewModel.submit(liked, comment, key.source) },
        onDismiss = coordinator::dismissDialog,
    )
}
