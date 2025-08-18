package com.github.xserxses.nqueensproblem.game.board.ui.model

import kotlin.time.Duration

sealed class GameBoardState {

    data object Loading : GameBoardState()

    data class Game(
        val state: STATE,
        val time: Duration,
        val moves: Int,
        val remainingQueens: Int,
        val ui: GameBoardUi
    ) : GameBoardState() {

        enum class STATE {
            PLAYING, BACKGROUND, PAUSED, FINISHED
        }
    }

    data class Error(
        val reason: Reason
    ) : GameBoardState() {
        enum class Reason {
            INVALID_SIZE, UNKNOWN
        }
    }
}
