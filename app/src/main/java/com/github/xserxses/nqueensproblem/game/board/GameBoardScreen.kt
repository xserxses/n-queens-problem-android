package com.github.xserxses.nqueensproblem.game.board

import ComposableLifecycle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.github.xserxses.nqueensproblem.game.board.ui.GameBoardViewModel
import com.github.xserxses.nqueensproblem.game.board.ui.composable.GameBoardComposable
import com.github.xserxses.nqueensproblem.game.board.ui.composable.GameBoardErrorComposable
import com.github.xserxses.nqueensproblem.game.board.ui.composable.GameBoardHudComposable
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardState
import kotlin.time.Duration

@Composable
fun GameBoardScreen(
    viewModel: GameBoardViewModel = hiltViewModel(),
    onFinish: (
        time: Duration,
        moves: Int,
        boardSize: Int
    ) -> Unit
) {
    val game = viewModel.state.collectAsState().value

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        ComposableLifecycle { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.onForegrounded()
                Lifecycle.Event.ON_STOP -> viewModel.onBackgrounded()
                else -> {}
            }
        }

        when (game) {
            is GameBoardState.Loading -> CircularProgressIndicator()
            is GameBoardState.Game -> {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    GameBoardHudComposable(
                        timeElapsed = game.time,
                        moves = game.moves,
                        remainingQueens = game.remainingQueens,
                        gameState = game.state,
                        onRestartClick = {
                            viewModel.onResetTapped()
                        },
                        onPlayPauseClick = {
                            viewModel.onPlayPauseClick()
                        }
                    )
                    GameBoardComposable(
                        cells = game.ui.cells,
                        onCellClick = { cell ->
                            viewModel.onCellTapped(cell)
                        }
                    )
                }
                if (game.state == GameBoardState.Game.STATE.FINISHED) {
                    onFinish(
                        game.time,
                        game.moves,
                        game
                            .ui
                            .cells
                            .maxBy { it.x }.x - 1
                    )
                }
            }

            is GameBoardState.Error -> GameBoardErrorComposable(game)
        }
    }
}

const val TAG = "GameBoardScreen"
