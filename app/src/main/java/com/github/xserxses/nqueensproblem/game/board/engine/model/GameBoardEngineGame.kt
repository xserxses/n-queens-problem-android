package com.github.xserxses.nqueensproblem.game.board.engine.model

data class GameBoardEngineGame(
    val status: Status,
    val queens: List<Coordinates>,
    val dangerCells: List<Coordinates>,
    val boardSize: Int,
    val moves: Int
) {
    enum class Status {
        IN_PROGRESS, FINISHED
    }

    data class Coordinates(val x: Int, val y: Int)
}
