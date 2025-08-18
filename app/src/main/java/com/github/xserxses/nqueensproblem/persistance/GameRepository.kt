package com.github.xserxses.nqueensproblem.persistance

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.time.Duration

class GameRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) {

    fun isSavedGameAvailable(): Boolean = restoreSavedGame() != null

    fun restoreSavedGame(): GameEntity? = runCatching {
        sharedPreferences
            .getString(GAME_KEY, null)
            .let {
                json.decodeFromString(GameEntity.serializer(), it ?: return null)
            }
    }.getOrNull()

    fun saveGame(game: GameEntity) {
        sharedPreferences.edit(commit = true) {
            putString(
                GAME_KEY,
                json.encodeToString(GameEntity.serializer(), game)
            )
        }
    }

    @Serializable
    data class GameEntity(
        val timeElapsed: Duration,
        val moves: Int,
        val boardSize: Int,
        val queensPlaced: List<Coordinates>
    )

    @Serializable
    data class Coordinates(val x: Int, val y: Int)

    private companion object {
        const val GAME_KEY = "game"
    }
}
