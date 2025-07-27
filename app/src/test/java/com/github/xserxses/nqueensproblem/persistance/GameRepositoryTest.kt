package com.github.xserxses.nqueensproblem.persistance

import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

internal class GameRepositoryTest {

    private lateinit var sharedPreferences: SharedPreferences
    private val json = Json

    private lateinit var gameRepository: GameRepository

    private val gameKey = "game"

    @BeforeEach
    fun `set up test environment`() {
        sharedPreferences = SPMockBuilder().createSharedPreferences()
        gameRepository = GameRepository(sharedPreferences, json)
    }

    @Test
    fun `isSavedGameAvailable should always return false as per current implementation`() {
        assertEquals(false, gameRepository.isSavedGameAvailable())
    }

    @Test
    fun `saveGame should serialize the game entity and save it to SharedPreferences`() {
        val gameEntity = GameRepository.GameEntity(
            timeElapsed = 65.seconds,
            boardSize = 8,
            queensPlaced = listOf(GameRepository.Coordinates(0, 0), GameRepository.Coordinates(1, 2))
        )

        val expectedJsonString = """
            {"timeElapsed":"PT1M5S","boardSize":8,"queensPlaced":[{"x":0,"y":0},{"x":1,"y":2}]}
        """.trimIndent()

        gameRepository.saveGame(gameEntity)

        val savedData = sharedPreferences.getString(gameKey, null)
        assertNotNull(savedData)
        assertEquals(expectedJsonString, savedData)
    }

    @Test
    fun `restoreSavedGame should return the correct GameEntity if a saved game exists`() {
        val gameEntity = GameRepository.GameEntity(
            timeElapsed = 120.seconds,
            boardSize = 4,
            queensPlaced = listOf(GameRepository.Coordinates(1, 3))
        )
        val jsonString = json.encodeToString(GameRepository.GameEntity.serializer(), gameEntity)
        sharedPreferences.edit().putString(gameKey, jsonString).commit()

        val restoredGame = gameRepository.restoreSavedGame()

        assertNotNull(restoredGame)
        assertEquals(gameEntity, restoredGame)
        assertEquals(120.seconds, restoredGame?.timeElapsed)
        assertEquals(4, restoredGame?.boardSize)
    }

    @Test
    fun `restoreSavedGame should return null if no saved game exists`() {
        sharedPreferences.edit().remove(gameKey).commit()

        val restoredGame = gameRepository.restoreSavedGame()

        assertNull(restoredGame)
    }

    @Test
    fun `restoreSavedGame should return null if the saved data is corrupt or invalid`() {
        val corruptJson = "{\"invalidJson\": true}"
        sharedPreferences.edit().putString(gameKey, corruptJson).commit()

        val restoredGame = gameRepository.restoreSavedGame()

        assertNull(restoredGame)
    }
}
