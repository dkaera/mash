package com.dkaera.mash.ui.onboarding.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import com.dkaera.mash.ui.onboarding.states.TextFieldState
import com.dkaera.mash.ui.onboarding.states.TextState

@Composable
fun TextInput(
    modifier: Modifier,
    label: String,
    textState: TextFieldState = remember { TextState() },
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = textState.text,
        onValueChange = { textState.text = it },
        label = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                textState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    textState.enableShowErrors()
                }
            },
        textStyle = MaterialTheme.typography.body2,
        isError = textState.showErrors(),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onImeAction() })
    )

    textState.getError()?.let { error -> TextFieldError(textError = error) }
}