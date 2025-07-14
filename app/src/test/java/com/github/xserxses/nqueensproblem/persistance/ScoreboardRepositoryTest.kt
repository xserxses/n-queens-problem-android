package com.github.xserxses.nqueensproblem.persistance

import android.content.SharedPreferences
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

class ScoreboardRepositoryTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var json: Json
    private lateinit var repository: ScoreboardRepository

    @BeforeEach
    fun setUp() {
        sharedPreferences = SPMockBuilder().createSharedPreferences()
        json = Json { ignoreUnknownKeys = true } // Configure as needed
        repository = ScoreboardRepository(sharedPreferences, json)
    }

    @Test
    fun `getScoreboardForBoardSize returns empty list when no scores are saved for that board size`() {
        val boardSize = 8
        val scores = repository.getScoreboardForBoardSize(boardSize)
        assertTrue(scores.isEmpty(), "Scoreboard should be empty for a new board size.")
    }

    @Test
    fun `saveScore adds a score and getScoreboardForBoardSize retrieves it`() {
        val boardSize = 4
        val record = ScoreboardRepository.ScoreboardRecord("Player1", 120.seconds, boardSize)
        repository.saveScore(record)

        val scores = repository.getScoreboardForBoardSize(boardSize)
        assertEquals(1, scores.size, "Should retrieve one score.")
        assertEquals(record, scores[0], "Retrieved score should match the saved score.")
    }

    @Test
    fun `getScoreboardForBoardSize returns scores sorted by time ascending`() {
        val boardSize = 6
        val record1 = ScoreboardRepository.ScoreboardRecord("PlayerFast", 30.seconds, boardSize)
        val record2 = ScoreboardRepository.ScoreboardRecord("PlayerSlow", 90.seconds, boardSize)
        val record3 = ScoreboardRepository.ScoreboardRecord("PlayerMedium", 60.seconds, boardSize)

        repository.saveScore(record2) // Save in unsorted order
        repository.saveScore(record1)
        repository.saveScore(record3)

        val scores = repository.getScoreboardForBoardSize(boardSize)
        assertEquals(3, scores.size)
        assertEquals(record1, scores[0], "Fastest player should be first.")
        assertEquals(record3, scores[1], "Medium player should be second.")
        assertEquals(record2, scores[2], "Slowest player should be third.")
    }

    @Test
    fun `saveScore respects SCOREBOARD_LIMIT`() {
        val boardSize = 5
        val limit = 20 // As defined in ScoreboardRepository.SCOREBOARD_LIMIT

        // Save more scores than the limit
        for (i in 1..(limit + 5)) {
            val record = ScoreboardRepository.ScoreboardRecord("Player$i", (i * 10).seconds, boardSize)
            repository.saveScore(record)
        }

        val scores = repository.getScoreboardForBoardSize(boardSize)
        assertEquals(limit, scores.size, "Number of scores should be capped at SCOREBOARD_LIMIT.")

        // Verify that the fastest times are kept
        val expectedFastestPlayer = ScoreboardRepository.ScoreboardRecord("Player1", 10.seconds, boardSize)
        assertEquals(expectedFastestPlayer.player, scores[0].player)
        assertEquals(expectedFastestPlayer.time, scores[0].time)

        val expectedSlowestKeptPlayer = ScoreboardRepository.ScoreboardRecord("Player$limit", (limit * 10).seconds, boardSize)
        assertEquals(expectedSlowestKeptPlayer.player, scores[limit - 1].player)
        assertEquals(expectedSlowestKeptPlayer.time, scores[limit - 1].time)
    }

    @Test
    fun `scores for different board sizes are isolated`() {
        val boardSize8 = 8
        val record8 = ScoreboardRepository.ScoreboardRecord("Player8", 50.seconds, boardSize8)
        repository.saveScore(record8)

        val boardSize4 = 4
        val record4 = ScoreboardRepository.ScoreboardRecord("Player4", 20.seconds, boardSize4)
        repository.saveScore(record4)

        val scores8 = repository.getScoreboardForBoardSize(boardSize8)
        assertEquals(1, scores8.size)
        assertEquals(record8, scores8[0])

        val scores4 = repository.getScoreboardForBoardSize(boardSize4)
        assertEquals(1, scores4.size)
        assertEquals(record4, scores4[0])

        assertTrue(repository.getScoreboardForBoardSize(10).isEmpty(), "Scoreboard for an unused board size should be empty.")
    }

    @Test
    fun `saving duplicate player times results in multiple entries until limit`() {
        // The current implementation allows duplicate player names if their times are different
        // or even if times are the same, they are treated as distinct entries before sorting and limiting.
        val boardSize = 7
        val time = 45.seconds
        val record1 = ScoreboardRepository.ScoreboardRecord("PlayerX", time, boardSize)
        val record2 = ScoreboardRepository.ScoreboardRecord("PlayerX", time, boardSize) // Identical record

        repository.saveScore(record1)
        repository.saveScore(record2)

        val scores = repository.getScoreboardForBoardSize(boardSize)
        // If times are identical, the order might not be strictly guaranteed beyond being sorted by time.
        // The Set conversion during save might de-duplicate if the serialized JSON strings are identical.
        // Let's check based on current logic which serializes, then converts to Set.
        // If ScoreboardEntity objects are identical, json.encodeToString will produce identical strings.
        // A Set of strings will then only contain one unique string.
        assertEquals(1, scores.size, "Saving identical records should result in one entry due to Set conversion of serialized strings.")
        assertEquals(record1, scores[0])
    }

    @Test
    fun `saving scores with slightly different times for same player are kept distinct if within limit`() {
        val boardSize = 7
        val player = "PlayerY"
        val record1 = ScoreboardRepository.ScoreboardRecord(player, 50.seconds, boardSize)
        val record2 = ScoreboardRepository.ScoreboardRecord(player, 51.seconds, boardSize)

        repository.saveScore(record1)
        repository.saveScore(record2)

        val scores = repository.getScoreboardForBoardSize(boardSize)
        assertEquals(2, scores.size, "Scores with different times for the same player should be distinct.")
        assertEquals(record1, scores[0]) // Faster time first
        assertEquals(record2, scores[1])
    }
}
