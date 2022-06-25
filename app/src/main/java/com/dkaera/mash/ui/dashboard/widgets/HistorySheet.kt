package com.dkaera.mash.ui.dashboard.widgets

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.dkaera.mash.ui.theme.pink500
import com.dkaera.mash.ui.util.lerp

enum class SheetState { Open, Closed }

val FabSize = 56.dp
private const val ExpandedSheetAlpha = 0.96f

@Composable
fun HistorySheet(
    openFraction: Float,
    width: Float,
    height: Float,
    updateSheet: (SheetState) -> Unit
) {
    // Use the fraction that the sheet is open to drive the transformation from FAB -> Sheet
    val fabSize = with(LocalDensity.current) { com.dkaera.mash.ui.dashboard.widgets.FabSize.toPx() }
    val fabSheetHeight = fabSize + WindowInsets.systemBars.getBottom(LocalDensity.current)
    val offsetX = lerp(width - fabSize, 0f, 0f, 0.15f, openFraction)
    val offsetY = lerp(height - fabSheetHeight, 0f, openFraction)
    val tlCorner = lerp(fabSize, 0f, 0f, 0.15f, openFraction)
    val surfaceColor = lerp(
        startColor = pink500,
        endColor = MaterialTheme.colors.primarySurface.copy(alpha = ExpandedSheetAlpha),
        startFraction = 0f,
        endFraction = 0.3f,
        fraction = openFraction
    )
    Surface(
        color = surfaceColor,
        contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.primarySurface),
        shape = RoundedCornerShape(topStart = tlCorner),
        modifier = Modifier.graphicsLayer {
            translationX = offsetX
            translationY = offsetY
        }
    ) {
        Records(openFraction, surfaceColor, updateSheet)
    }
}