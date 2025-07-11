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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameBoardEngineGame

        if (reminingQueens != other.reminingQueens) return false
        if (status != other.status) return false
        if (!board.contentDeepEquals(other.board)) return false
        if (timeElapsed != other.timeElapsed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = reminingQueens
        result = 31 * result + status.hashCode()
        result = 31 * result + board.contentDeepHashCode()
        result = 31 * result + timeElapsed.hashCode()
        return result
    }
}
