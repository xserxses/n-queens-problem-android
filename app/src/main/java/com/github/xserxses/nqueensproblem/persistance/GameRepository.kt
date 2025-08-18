package com.github.xserxses.nqueensproblem.persistance

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.time.Duration

class GameRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) {

    fun isSavedGameAvailable(): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == GAME_KEY) {
                trySend(restoreSavedGame() != null)
            }
        }
        // Emit initial value
        trySend(restoreSavedGame() != null)

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        // This will be called when the flow is cancelled
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.conflate()

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

    fun removeGame() {
        sharedPreferences.edit(commit = true){
            remove(GAME_KEY)
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
