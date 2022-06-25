package com.dkaera.mash.ui.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dkaera.mash.Graph
import com.dkaera.mash.R
import com.dkaera.mash.ui.dashboard.widgets.FabSize
import com.dkaera.mash.ui.dashboard.widgets.HistorySheet
import com.dkaera.mash.ui.dashboard.widgets.SheetState
import com.dkaera.mash.ui.theme.MashTheme
import com.dkaera.mash.ui.theme.PinkTheme
import kotlinx.coroutines.launch
import kotlin.math.ceil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dashboard(
    onLogout: () -> Unit
) {
    PinkTheme {
        BoxWithConstraints {
            val sheetState = rememberSwipeableState(SheetState.Closed)
            val fabSize = with(LocalDensity.current) { FabSize.toPx() }
            val dragRange = constraints.maxHeight - fabSize
            val scope = rememberCoroutineScope()

            BackHandler(
                enabled = sheetState.currentValue == SheetState.Open,
                onBack = {
                    scope.launch { sheetState.animateTo(SheetState.Closed) }
                }
            )

            Box(
                // The Lessons sheet is initially closed and appears as a FAB. Make it openable by
                // swiping or clicking the FAB.
                Modifier.swipeable(
                    state = sheetState,
                    anchors = mapOf(
                        0f to SheetState.Closed,
                        -dragRange to SheetState.Open
                    ),
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical
                )
            ) {
                val openFraction = if (sheetState.offset.value.isNaN()) {
                    0f
                } else {
                    -sheetState.offset.value / dragRange
                }.coerceIn(0f, 1f)
                DashboardContent(onLogout)
                HistorySheet(
                    openFraction,
                    this@BoxWithConstraints.constraints.maxWidth.toFloat(),
                    this@BoxWithConstraints.constraints.maxHeight.toFloat()
                ) { state -> scope.launch { sheetState.animateTo(state) } }
            }
        }
    }
}

@Composable
private fun DashboardContent(
    onLogout: () -> Unit,
) {
    val viewModel: DashboardViewModel = Graph.dashboardViewModel
    val listState = remember { viewModel.numbersState }
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = { AppBar(onLogout) },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                StaggeredVerticalGrid(
                    maxColumnWidth = 220.dp,
                    modifier = Modifier.padding(4.dp)
                ) {
                    listState.forEach { value ->
                        NumberItem(value, {
                            if (listState.remove(value)) {
                                viewModel.add(value)
                            }
                        })
                    }
                }
            }
        }
    )
}

@Composable
private fun AppBar(
    onLogout: () -> Unit,
) {
    TopAppBar(elevation = 0.dp) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = onLogout
        ) {
            Image(
                imageVector = Icons.Filled.Logout,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier
                    .padding(16.dp)
                    .rotate(180f)
                    .align(Alignment.CenterVertically)
            )
        }
        Text(
            text = stringResource(id = R.string.dashboard),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = {
                Graph.dashboardViewModel.apply {
                    removeAll()
                    generateFibonacci()
                }
            }
        ) {
            Image(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    maxColumnWidth: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            "Unbounded width not supported"
        }
        val columns = ceil(constraints.maxWidth / maxColumnWidth.toPx()).toInt()
        val columnWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val colHeights = IntArray(columns) { 0 } // track each column's height
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(colHeights)
            val placeable = measurable.measure(itemConstraints)
            colHeights[column] += placeable.height
            placeable
        }

        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val colY = IntArray(columns) { 0 }
            placeables.forEach { placeable ->
                val column = shortestColumn(colY)
                placeable.place(
                    x = columnWidth * column,
                    y = colY[column]
                )
                colY[column] += placeable.height
            }
        }
    }
}

private fun shortestColumn(colHeights: IntArray): Int {
    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}

@Composable
fun NumberItem(
    number: Int,
    selectItem: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(16.dp),
        color = MaterialTheme.colors.surface,
        elevation = MashTheme.elevations.card,
        shape = MaterialTheme.shapes.medium
    ) {
        ConstraintLayout(
            modifier = Modifier
                .clickable(onClick = {
                    selectItem(number)
                })
                .semantics { contentDescription = "content_description" }
        ) {
            val (subject) = createRefs()
            Text(
                text = number.toString(),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(1.0f)
                    .constrainAs(subject) {
                        centerHorizontallyTo(parent)
                    }
            )
        }
    }
}




