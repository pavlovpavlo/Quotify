package com.kovhan.feature.main.presentation.faq

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kovhan.core.ui.component.QuotifyTopBar
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R
import com.kovhan.feature.main.presentation.faq.component.FaqAccordionItem
import com.kovhan.feature.main.presentation.faq.model.faqSections
import com.kovhan.feature.main.presentation.faq.navigation.FaqScreenNavAction

@Composable
fun FaqScreen(
    navAction: FaqScreenNavAction,
    paddingValues: PaddingValues,
) {
    val colors = QuotifyMaterialTheme.colors
    val typography = QuotifyMaterialTheme.typography
    val cardShape = RoundedCornerShape(QuotifyMaterialTheme.dimensions.radiusLg)

    // Розкрите питання одне на весь екран — так відповідь не губиться серед
    // сусідніх, а список не розповзається на кілька екранів прокрутки.
    var expandedQuestion by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        QuotifyTopBar(
            title = stringResource(R.string.faq_title),
            onBack = navAction::navigateBack,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = paddingValues.calculateBottomPadding() + 24.dp,
                ),
        ) {
            faqSections.forEachIndexed { sectionIndex, section ->
                Spacer(Modifier.height(if (sectionIndex == 0) 4.dp else 20.dp))

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(section.title),
                    color = colors.textSecondary,
                    style = typography.eyebrow.copy(fontWeight = FontWeight.W600),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, cardShape)
                        .clip(cardShape)
                        .background(colors.bgElevated)
                        .border(1.dp, colors.border, cardShape),
                ) {
                    section.entries.forEachIndexed { index, entry ->
                        FaqAccordionItem(
                            question = stringResource(entry.question),
                            answer = stringResource(entry.answer),
                            expanded = expandedQuestion == entry.question,
                            onToggle = {
                                expandedQuestion = if (expandedQuestion == entry.question) {
                                    null
                                } else {
                                    entry.question
                                }
                            },
                            showDivider = index != section.entries.lastIndex,
                        )
                    }
                }
            }
        }
    }
}
