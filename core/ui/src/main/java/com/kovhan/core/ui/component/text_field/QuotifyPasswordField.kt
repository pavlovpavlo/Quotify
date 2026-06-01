package com.kovhan.core.ui.component.text_field

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kovhan.design.systems.QuotifyMaterialTheme
import com.kovhan.design.systems.R

/**
 * Password input that toggles between obscured and visible text via an eye icon
 * trailing affordance. Wraps [QuotifyTextField] so border/colors/typography stay
 * identical to every other field on the screen.
 */
@Composable
fun QuotifyPasswordField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    modifierContainer: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    placeholder: String = "",
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Done,
    textStyle: TextStyle = QuotifyMaterialTheme.typography.body,
    @StringRes error: Int? = null,
) {
    var visible by remember { mutableStateOf(false) }
    val transformation: VisualTransformation =
        if (visible) VisualTransformation.None else PasswordVisualTransformation()

    QuotifyTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        modifierContainer = modifierContainer,
        label = label,
        required = required,
        placeholder = placeholder,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        mask = transformation,
        textStyle = textStyle,
        error = error,
        trailingIcon = {
            val interactionSource = remember { MutableInteractionSource() }
            val tint = QuotifyMaterialTheme.colors.textTertiary
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(color = tint, bounded = false, radius = 18.dp),
                    ) { visible = !visible },
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(
                        if (visible) R.drawable.ic_eye_off else R.drawable.ic_eye,
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(tint),
                )
            }
        },
    )
}
