package com.github.xserxses.nqueensproblem.persistance

import android.content.SharedPreferences
import androidx.core.content.edit
import jakarta.inject.Inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class ScoreboardRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val json: Json
) {
    data class ScoreboardRecord(
        val player: String,
        val time: Duration,
        val boardSize: Int
    )

    fun getScoreboardForBoardSize(boardSize: Int): List<ScoreboardRecord> =
        readEntitiesFromSharedPrefs(boardSize)
            ?.map { ScoreboardRecord(it.player, it.timeMs.milliseconds, boardSize) }
            ?.sortedBy { it.time }
            ?: emptyList()

    private fun readEntitiesFromSharedPrefs(boardSize: Int): List<ScoreboardEntity>? =
        sharedPreferences
            .getStringSet(SCOREBOARD_KEY_PREFIX + boardSize, emptySet())
            ?.map { json.decodeFromString(ScoreboardEntity.serializer(), it) }

    fun saveScore(score: ScoreboardRecord) {
        val existing = readEntitiesFromSharedPrefs(score.boardSize)
            ?: emptyList()

        val newEntity = ScoreboardEntity(
            player = score.player,
            timeMs = score.time.inWholeMilliseconds
        )

        val newSet = (existing + newEntity).sortedBy { it.timeMs }
            .map { json.encodeToString(ScoreboardEntity.serializer(), it) }
            .take(SCOREBOARD_LIMIT)
            .toSet()

        sharedPreferences
            .edit {
                putStringSet(SCOREBOARD_KEY_PREFIX + score.boardSize, newSet)
            }
    }

    private companion object {
        const val SCOREBOARD_KEY_PREFIX = "scoreboard_"
        const val SCOREBOARD_LIMIT = 20
    }

    @Serializable
    private data class ScoreboardEntity(
        val player: String,
        val timeMs: Long
    )
}
