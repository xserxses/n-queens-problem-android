package com.github.xserxses.nqueensproblem.game.board.engine.model

import kotlin.time.Duration

data class GameBoardEngineGame(
    val status: Status,
    val reminingQueens: Int,
    val board: Array<Array<Cell>>,
    val timeElapsed: Duration

) {
    data class Cell(
        val hasQueen: Boolean,
        val hasDanger: Boolean
    )
    enum class Status {
        IN_PROGRESS, FINISHED
    }
}
