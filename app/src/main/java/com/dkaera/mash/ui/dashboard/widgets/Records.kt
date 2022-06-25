package com.dkaera.mash.ui.dashboard.widgets

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dkaera.mash.Graph
import com.dkaera.mash.R
import com.dkaera.mash.domain.entity.NumberModel
import com.dkaera.mash.ui.theme.MashTheme
import com.dkaera.mash.ui.util.lerp
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
internal fun Records(
    openFraction: Float,
    surfaceColor: Color = MaterialTheme.colors.surface,
    updateSheet: (SheetState) -> Unit,
) {
    val records = remember { Graph.dashboardViewModel.savedNumbersState }

    Box(modifier = Modifier.fillMaxWidth()) {
        // When sheet open, show a list of the lessons
        val lessonsAlpha = lerp(0f, 1f, 0.2f, 0.8f, openFraction)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = lessonsAlpha }
                .statusBarsPadding()
        ) {
            val scroll = rememberLazyListState()
            val appBarElevation by animateDpAsState(if (scroll.isScrolled) 4.dp else 0.dp)
            val appBarColor = if (appBarElevation > 0.dp) surfaceColor else Color.Transparent
            TopAppBar(
                backgroundColor = appBarColor,
                elevation = appBarElevation
            ) {
                Text(
                    text = stringResource(id = R.string.history),
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = { updateSheet(SheetState.Closed) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ExpandMore,
                        contentDescription = stringResource(R.string.label_collapse_records)
                    )
                }
            }
            if (records.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Center),
                        color = MaterialTheme.colors.surface,
                        elevation = MashTheme.elevations.card,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "No items yet",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.subtitle1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = scroll,
                    contentPadding = WindowInsets.systemBars
                        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                        .asPaddingValues()
                ) {
                    items(
                        items = records,
                        key = { it }
                    ) { record ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { Graph.dashboardViewModel.remove(record.number) }
                                .padding(16.dp),
                            color = MaterialTheme.colors.surface,
                            elevation = MashTheme.elevations.card,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row {
                                Text(
                                    text = record.number.toString(),
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.subtitle1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(0.3f)
                                        .padding(16.dp)
                                )
                                Text(
                                    text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                                        .format(Date().apply { time = record.date }),
                                    color = MaterialTheme.colors.primary,
                                    style = MaterialTheme.typography.subtitle1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .weight(0.7f)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // When sheet closed, show the FAB
        val fabAlpha = lerp(1f, 0f, 0f, 0.15f, openFraction)
        Box(
            modifier = Modifier
                .size(FabSize)
                .padding(start = 16.dp, top = 8.dp) // visually center contents
                .graphicsLayer { alpha = fabAlpha }
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = { updateSheet(SheetState.Open) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlaylistPlay,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = stringResource(R.string.label_expand_records)
                )
            }
        }
    }
}

private val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0