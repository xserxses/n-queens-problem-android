package com.github.xserxses.nqueensproblem.scoreboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.scoreboard.ui.ScoreboardState
import com.github.xserxses.nqueensproblem.scoreboard.ui.ScoreboardViewModel
import com.github.xserxses.nqueensproblem.ui.shared.NumberPickerComposable
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import com.github.xserxses.nqueensproblem.utils.GameBoardSizeValueValidator
import kotlin.time.Duration.Companion.seconds


@Composable
fun ScoreboardScreen(
    viewModel: ScoreboardViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onSizeChanged(INITIAL_SIZE)
    }
    ScoreboardScreenContent(
        state.value,
        { viewModel.onSizeChanged(it) })
}

@Composable
private fun ScoreboardScreenContent(
    state: ScoreboardState,
    onSizeChanged: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.scoreboard_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.scoreboard_subtitle),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall
        )
        NumberPickerComposable(
            modifier = Modifier
                .padding(8.dp),
            initialValue = INITIAL_SIZE,
            valueValidator = GameBoardSizeValueValidator,
            onValueChange = onSizeChanged
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (state) {
                is ScoreboardState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                ScoreboardState.Empty -> item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.scoreboard_empty),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

                is ScoreboardState.Error -> item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.scoreboard_error),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

                is ScoreboardState.Scoreboard -> itemsIndexed(
                    state.items
                ) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    item.player,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            supportingContent = { Text(item.time.toString()) },
                            leadingContent = {
                                Text(
                                    text = "${index + 1}.",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }

            }
        }
    }
}

@PreviewLightDark
@Composable
fun ScoreboardScreenContentLoadingPreview() {
    NQueensProblemTheme {
        ScoreboardScreenContent(
            state = ScoreboardState.Loading,
            onSizeChanged = {}
        )
    }
}

@PreviewLightDark
@Composable
fun ScoreboardScreenContentEmptyPreview() {
    NQueensProblemTheme {
        ScoreboardScreenContent(
            state = ScoreboardState.Empty,
            onSizeChanged = {}
        )
    }
}

@PreviewLightDark
@Composable
fun ScoreboardScreenContentErrorPreview() {
    NQueensProblemTheme {
        ScoreboardScreenContent(
            state = ScoreboardState.Error("An unexpected error occurred"),
            onSizeChanged = {}
        )
    }
}

@PreviewLightDark
@Composable
fun ScoreboardScreenContentWithItemsPreview() {
    val items = listOf(
        ScoreboardState.Scoreboard.ScoreboardItem("Xserxses", 30.seconds),
        ScoreboardState.Scoreboard.ScoreboardItem("Marek", 45.seconds),
        ScoreboardState.Scoreboard.ScoreboardItem("Player 3", 60.seconds)
    )
    NQueensProblemTheme {
        ScoreboardScreenContent(
            state = ScoreboardState.Scoreboard(items),
            onSizeChanged = {}
        )
    }
}

private const val INITIAL_SIZE = 8
