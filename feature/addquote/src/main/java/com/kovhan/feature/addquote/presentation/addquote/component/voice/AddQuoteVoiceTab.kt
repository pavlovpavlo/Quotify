package com.kovhan.feature.addquote.presentation.addquote.component.voice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.content.ContextCompat
import com.kovhan.core.ui.component.quote.QuoteTextInputField
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

@Composable
internal fun AddQuoteVoiceTab(
    quote: TextFieldValue,
    isRecording: Boolean,
    isVoiceAvailable: Boolean,
    onQuoteChange: (TextFieldValue) -> Unit,
    onMicPressed: () -> Unit,
    onMicReleased: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = QuotifyMaterialTheme.colors
    val dimensions = QuotifyMaterialTheme.dimensions
    val typography = QuotifyMaterialTheme.typography

    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted -> hasPermission = granted }

    val startListening = {
        when {
            !isVoiceAvailable -> Unit
            !hasPermission -> permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            else -> onMicPressed()
        }
    }

    val hint = when {
        !isVoiceAvailable -> stringResource(R.string.add_quote_voice_unavailable)
        isRecording -> stringResource(R.string.add_quote_voice_hint_listening)
        else -> stringResource(R.string.add_quote_voice_hint_idle)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = dimensions.size4),
        ) {
            if (quote.text.isBlank()) {
                VoiceEmptyState()
            } else {
                QuoteTextInputField(
                    value = quote,
                    onValueChange = onQuoteChange,
                    label = stringResource(R.string.add_quote_voice_field_label),
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.size18, bottom = dimensions.size8),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.size14),
        ) {
            VoiceWave(active = isRecording)
            VoiceMicButton(
                recording = isRecording,
                onStart = startListening,
                onStop = onMicReleased,
                enabled = isVoiceAvailable,
            )
            Text(
                text = hint,
                style = typography.caption,
                color = colors.textTertiary,
            )
        }
    }
}
