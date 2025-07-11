package com.github.xserxses.nqueensproblem.game.board.ui.model

sealed class GameBoardState {

    data object Loading : GameBoardState()

    data class Game(
        val ui: GameBoardUi,
        val win: GameBoardWin?
    ) : GameBoardState() {
        data class GameBoardWin(
            val time: Long,
            val moves: Int
        )
    }

    data class Error(
        val reason: ErrorReason,
        val message: String?
    ) : GameBoardState() {

        enum class ErrorReason {
            NO_SAVED_GAME_FOUND,
        }
    }
}
