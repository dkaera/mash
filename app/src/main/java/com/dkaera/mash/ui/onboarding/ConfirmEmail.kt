package com.dkaera.mash.ui.onboarding

import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmEmail(modifier: Modifier, authComplete: () -> Unit) {
    Surface() {
        Text(text = "ConfirmEmail")
    }
}

@Preview(name = "ConfirmEmail")
@Composable
private fun ConfirmEmailPreview() = ConfirmEmail(Modifier) { }