package com.github.xserxses.nqueensproblem.persistance

import javax.inject.Inject
import kotlin.time.Duration

class GameRepository @Inject constructor() {

    fun isSavedGameAvailable(): Boolean = false

    fun restoreSavedGame(): GameEntity? = TODO()

    fun saveGame(game: GameEntity) {

    }

    data class GameEntity(
        val timeElapsed: Duration,
        val boardSize: Int,
        val queensPlaced: List<Coordinates>,
    )

    data class Coordinates(val x: Int, val y: Int)
}
