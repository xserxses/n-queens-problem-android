package com.github.xserxses.nqueensproblem.game.finish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.main.naviagation.GameFinish
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameFinishScreen(
    viewModel: GameFinishViewModel = hiltViewModel(),
    onNavigateMainMenu: () -> Unit,
    onNavigateScoreboard: (boardSize: Int?) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        GameFinishScreenContent(
            gameFinish = viewModel.gameFinish,
            saveRecord = { text -> viewModel.saveRecord(text) },
            finishGame = { viewModel.finishGame() },
            onNavigateMainMenu = onNavigateMainMenu,
            onNavigateScoreboard = onNavigateScoreboard
        )
    }
}

@Composable
private fun GameFinishScreenContent(
    gameFinish: GameFinish,
    saveRecord: (name: String) -> Unit,
    finishGame: () -> Unit,
    onNavigateMainMenu: () -> Unit,
    onNavigateScoreboard: (boardSize: Int?) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.game_finish_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(
                    R.string.game_finish_subtitle,
                    gameFinish.boardSize,
                    gameFinish.timeMillis.milliseconds.inWholeSeconds,
                    gameFinish.moves
                ),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { newText ->
                        isError = newText.isEmpty()
                        text = newText
                    },
                    label = {
                        Text(
                            text = "Enter Your name",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = if (isError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),
                    isError = isError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )
            }
            Spacer(Modifier.size(32.dp))
            Button(
                onClick = {
                    if (text.isNotEmpty()) {
                        saveRecord(text)
                        finishGame()
                        onNavigateScoreboard(gameFinish.boardSize)
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.game_finish_go_to_scoreboard)
                )
            }
            Spacer(Modifier.size(8.dp))
            Button(
                onClick = {
                    finishGame()
                    onNavigateMainMenu()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.game_finish_close)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun GameFinishScreenContentPreview() {
    NQueensProblemTheme {
        GameFinishScreenContent(
            gameFinish = GameFinish(boardSize = 8, timeMillis = 120000, moves = 15),
            saveRecord = {},
            finishGame = {},
            onNavigateMainMenu = {},
            onNavigateScoreboard = {}
        )
    }
}
