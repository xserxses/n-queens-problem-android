package com.github.xserxses.nqueensproblem.game.board.engine.model

sealed class GameBoardEngineStartMode {
    data class New(val boardSize: Int) : GameBoardEngineStartMode()
    data object Continue : GameBoardEngineStartMode()
}
