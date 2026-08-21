package com.kovhan.feature.survey.presentation.survey.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kovhan.core.models.survey.SurveyOptionInput
import com.kovhan.core.ui.component.quote.QuoteTextInputField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R as DsR

/** Extra free-text field revealed by the option that asked for it. */
@Composable
internal fun SurveyOptionInputField(
    fieldKey: String,
    config: SurveyOptionInput,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = QuotifyMaterialTheme.dimensions

    var fieldValue by remember(fieldKey) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    QuoteTextInputField(
        modifier = modifier.fillMaxWidth(),
        value = fieldValue,
        onValueChange = {
            fieldValue = it
            onValueChange(it.text)
        },
        label = config.label ?: stringResource(DsR.string.survey_input_label),
        placeholder = config.placeholder ?: stringResource(DsR.string.survey_input_hint),
        minHeight = dimensions.size96,
    )
}
