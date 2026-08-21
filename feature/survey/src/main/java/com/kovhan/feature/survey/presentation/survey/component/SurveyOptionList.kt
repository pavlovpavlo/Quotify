package com.kovhan.feature.survey.presentation.survey.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kovhan.core.models.survey.SurveyQuestion
import com.kovhan.design.systems.QuotifyMaterialTheme

@Composable
internal fun SurveyOptionList(
    question: SurveyQuestion,
    selectedOptions: Set<String>,
    inputs: Map<String, String>,
    onOptionClick: (String) -> Unit,
    onInputChange: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensions.size9),
    ) {
        question.options.forEach { option ->
            val selected = option.id in selectedOptions

            SurveyOptionRow(
                label = option.text,
                type = question.type,
                selected = selected,
                onClick = { onOptionClick(option.id) },
            )

            val input = option.input
            if (input != null) {
                AnimatedVisibility(
                    visible = selected,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    SurveyOptionInputField(
                        modifier = Modifier.padding(top = dimensions.size3),
                        fieldKey = "${question.id}_${option.id}",
                        config = input,
                        value = inputs[option.id].orEmpty(),
                        onValueChange = { text -> onInputChange(option.id, text) },
                    )
                }
            }
        }
    }
}
