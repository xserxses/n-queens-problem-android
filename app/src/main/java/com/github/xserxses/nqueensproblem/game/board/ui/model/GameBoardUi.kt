package com.github.xserxses.nqueensproblem.game.board.ui.model

data class GameBoardUi(
    val cells: Array<Array<GameBoardElementUi>>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameBoardUi

        return cells.contentDeepEquals(other.cells)
    }

    override fun hashCode(): Int {
        return cells.contentDeepHashCode()
    }
}

sealed class GameBoardElementUi {
    data class Border(val text: String) : GameBoardElementUi()
    data class Cell(
        val x: Int,
        val y: Int,
        val hasQueen: Boolean,
        val state: CellState
    ) {
        enum class CellState { NORMAL, DANGER }
    }
}
