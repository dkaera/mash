package com.dkaera.mash.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.dkaera.mash.ui.theme.PurpleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PurpleTheme {
                MashApp(finishActivity = { finish() })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PurpleTheme {
        MashApp(finishActivity = {  })
    }
}