package com.kovhan.core.ui.component.emptystate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.button.QuotifyButton
import com.kovhan.core.ui.component.button.QuotifyButtonDefaults
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

@Composable
fun DefaultEmptyState(
    modifier: Modifier = Modifier,
    imageResource: Int? = QuotifyMaterialTheme.images.imgLibraryEmpty,
    titleResource: Int? = DsR.string.search_empty_title,
    descriptionResource: Int? = DsR.string.search_empty_sub,
    action: (@Composable () -> Unit)? = null,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.size24, ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(dimensions.size44))

        imageResource?.let{
            Image(
                modifier = Modifier.width(
                    dimensions.size232
                ),
                painter = painterResource(imageResource),
                contentDescription = "EmptyState image"
            )
        }


        titleResource?.let{
            Spacer(modifier = Modifier.height(dimensions.size6))

            Text(
                text = stringResource(titleResource),
                style = typography.h4,
                color = colors.textPrimary,
                textAlign = TextAlign.Center,
            )
        }

        descriptionResource?.let{
            Spacer(modifier = Modifier.height(dimensions.size6))

            Text(
                modifier = Modifier.widthIn(max = 240.dp),
                text = stringResource(descriptionResource),
                style = typography.body,
                color = colors.textTertiary,
                textAlign = TextAlign.Center,
            )
        }

        action?.let {
            Spacer(modifier = Modifier.height(dimensions.size20))

            it()
        }
    }
}

@Composable
fun DefaultEmptyStateAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    QuotifyButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        sizeSpec = QuotifyButtonDefaults
            .pillSizeSpec(height = QuotifyMaterialTheme.dimensions.size52)
            .copy(horizontalPadding = EmptyStateActionPadding),
    )
}

private val EmptyStateActionPadding = 30.dp
