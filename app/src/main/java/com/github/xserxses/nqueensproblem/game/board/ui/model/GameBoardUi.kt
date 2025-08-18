package com.github.xserxses.nqueensproblem.game.board.ui.model

import java.util.Objects

data class GameBoardUi(
    val cells: List<GameBoardElementUi>
)

sealed class GameBoardElementUi(
    open val id: Long,
    open val x: Int,
    open val y: Int

) {

    data class Placeholder(
        override val x: Int,
        override val y: Int
    ) : GameBoardElementUi(
        id = Objects.hash(x, y, "placeholder_type").toLong(),
        x = x,
        y = y
    )

    data class Border(
        val text: String,
        override val x: Int,
        override val y: Int
    ) : GameBoardElementUi(
        id = Objects.hash(x, y, text, "border_type").toLong(),
        x = x,
        y = y
    )

    data class Cell(
        override val x: Int,
        override val y: Int,
        val hasQueen: Boolean,
        val state: CellState,
        val color: CellColor
    ) : GameBoardElementUi(
        id = Objects.hash(x, y, "cell_type").toLong(),
        x = x,
        y = y
    ) {
        enum class CellState { NORMAL, DANGER }
        enum class CellColor { DARK, LIGHT }
    }
}
