package com.github.xserxses.nqueensproblem.game.board.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardState
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import com.github.xserxses.nqueensproblem.utils.formatGameTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun GameBoardHudComposable(
    timeElapsed: Duration,
    moves: Int,
    remainingQueens: Int,
    gameState: GameBoardState.Game.STATE,
    onPlayPauseClick: () -> Unit,
    onRestartClick: () -> Unit
) {
    Row {
        Column {
            Text(
                text = stringResource(R.string.game_time, formatGameTime(timeElapsed)),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.game_moves, moves),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.game_remining_queens, remainingQueens),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.weight(1f))
        Column {
            PlayPauseButton(
                isPlaying = gameState == GameBoardState.Game.STATE.PLAYING,
                onPlayPauseClick = onPlayPauseClick
            )
            FilledIconButton(
                onClick = { onRestartClick() },
                colors = IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(
                    painterResource(R.drawable.icon_replay),
                    contentDescription = stringResource(
                        R.string.game_pause_cd
                    )
                )
            }
        }
    }
}

@Composable
private fun PlayPauseButton(
    isPlaying: Boolean = true,
    onPlayPauseClick: () -> Unit
) {
    FilledIconButton(
        onClick = onPlayPauseClick,
        colors = IconButtonDefaults.filledIconButtonColors()
    ) {
        if (isPlaying) {
            Icon(
                painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(
                    R.string.game_pause_cd
                )
            )
        } else {
            Icon(
                painterResource(R.drawable.icon_play),
                contentDescription = stringResource(
                    R.string.game_play_cd
                )
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun GameBoardHudComposablePreview_Playing() {
    NQueensProblemTheme {
        GameBoardHudComposable(
            timeElapsed = 12.seconds,
            moves = 8,
            gameState = GameBoardState.Game.STATE.PLAYING,
            remainingQueens = 4,
            onPlayPauseClick = {},
            onRestartClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun GameBoardHudComposablePreview_Paused() {
    NQueensProblemTheme {
        GameBoardHudComposable(
            timeElapsed = 12.seconds,
            moves = 10,
            gameState = GameBoardState.Game.STATE.PAUSED,
            remainingQueens = 4,
            onPlayPauseClick = {},
            onRestartClick = {}
        )
    }
}
