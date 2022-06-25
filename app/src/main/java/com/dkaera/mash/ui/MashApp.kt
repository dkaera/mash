package com.dkaera.mash.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun MashApp(finishActivity: () -> Unit) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
    ) { innerPaddingModifier ->
        NavGraph(
            finishActivity = finishActivity,
            navController = rememberNavController(),
            modifier = Modifier.padding(innerPaddingModifier)
        )
    }
}