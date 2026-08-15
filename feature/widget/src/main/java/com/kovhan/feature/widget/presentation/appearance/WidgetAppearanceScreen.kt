package com.kovhan.feature.widget.presentation.appearance

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kovhan.core.models.widget.WidgetStyleSettings
import com.kovhan.core.models.widget.WidgetTextColor
import com.kovhan.core.ui.component.color.QuotifyColorPickerDialog
import com.kovhan.core.ui.component.widget.WidgetPreviewCard
import com.kovhan.core.ui.component.widget.WidgetPreviewQuote
import com.kovhan.core.ui.mapper.WidgetBackgroundMapper
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR
import com.kovhan.feature.widget.presentation.appearance.component.WidgetAppearanceToggleRow
import com.kovhan.feature.widget.presentation.appearance.component.WidgetBackgroundSection
import com.kovhan.feature.widget.presentation.appearance.component.WidgetBorderSection
import com.kovhan.feature.widget.presentation.appearance.component.WidgetFontSizeTabs
import com.kovhan.feature.widget.presentation.appearance.component.WidgetLabeledSection
import com.kovhan.feature.widget.presentation.appearance.component.WidgetTextAlignTabs
import com.kovhan.feature.widget.presentation.appearance.component.WidgetTextColorTabs
import com.kovhan.feature.widget.presentation.appearance.mvi.WidgetAppearanceIntent
import com.kovhan.feature.widget.presentation.appearance.mvi.WidgetAppearanceState
import com.kovhan.feature.widget.presentation.settings.component.WidgetSettingsFooter
import com.kovhan.feature.widget.presentation.settings.component.WidgetSettingsTopBar

private val PREVIEW_WIDTH = 260.dp

@Composable
internal fun WidgetAppearanceScreen(
    state: WidgetAppearanceState,
    intent: WidgetAppearanceIntent,
    onBack: () -> Unit,
    onSave: () -> Unit,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions

    BackHandler(onBack = onBack)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary,
            )
            return@Box
        }

        val settings = state.settings

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        ) {
            WidgetSettingsTopBar(
                title = stringResource(DsR.string.widget_appearance_title),
                onBack = onBack,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = dimensions.space5,
                        end = dimensions.space5,
                        top = dimensions.space2,
                        bottom = dimensions.space6,
                    ),
                verticalArrangement = Arrangement.spacedBy(dimensions.space6),
            ) {
                WidgetPreviewCard(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(PREVIEW_WIDTH)
                        .aspectRatio(1f),
                    settings = settings,
                    quote = WidgetPreviewQuote(
                        text = stringResource(DsR.string.widget_appearance_preview_quote),
                        author = stringResource(DsR.string.widget_appearance_preview_author),
                        book = stringResource(DsR.string.widget_appearance_preview_book),
                    ),
                )

                WidgetBackgroundSection(settings = settings, intent = intent)

                if (settings is WidgetStyleSettings.Cover) {
                    WidgetAppearanceToggleRow(
                        title = stringResource(DsR.string.widget_appearance_blur_title),
                        subtitle = stringResource(DsR.string.widget_appearance_blur_subtitle),
                        checked = settings.blurEnabled,
                        onCheckedChange = intent::onBlurToggled,
                    )
                }

                WidgetBorderSection(settings = settings, intent = intent)

                WidgetLabeledSection(stringResource(DsR.string.widget_appearance_font_size)) {
                    WidgetFontSizeTabs(
                        selected = settings.fontSize,
                        onSelect = intent::onFontSizeSelected,
                    )
                }

                WidgetLabeledSection(stringResource(DsR.string.widget_appearance_text_color)) {
                    WidgetTextColorTabs(
                        settings = settings,
                        onLight = intent::onTextColorLight,
                        onDark = intent::onTextColorDark,
                        onCustom = intent::onCustomTextColorRequested,
                    )
                }

                WidgetLabeledSection(stringResource(DsR.string.widget_appearance_align)) {
                    WidgetTextAlignTabs(
                        selected = settings.textAlign,
                        onSelect = intent::onTextAlignSelected,
                    )
                }
            }

            WidgetSettingsFooter(
                text = stringResource(DsR.string.widget_appearance_done),
                onClick = onSave,
            )
        }

        if (state.pickingCustomTextColor) {
            QuotifyColorPickerDialog(
                initialColor = currentCustomColor(settings, QuotifyMaterialTheme.system.isDarkTheme),
                onConfirm = { intent.onCustomTextColorPicked(it.toArgb()) },
                onDismiss = intent::onCustomTextColorDismissed,
            )
        }
    }
}

private fun currentCustomColor(settings: WidgetStyleSettings, darkTheme: Boolean): Color =
    (settings.textColor as? WidgetTextColor.Custom)
        ?.let { Color(it.argb) }
        ?: WidgetBackgroundMapper.textColor(settings, darkTheme)
