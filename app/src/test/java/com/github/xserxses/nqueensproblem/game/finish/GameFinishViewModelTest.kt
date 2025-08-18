
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.github.xserxses.nqueensproblem.game.finish.GameFinishViewModel
import com.github.xserxses.nqueensproblem.main.naviagation.GameFinish
import com.github.xserxses.nqueensproblem.persistance.ScoreboardRepository
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds

class GameFinishViewModelTest {

    private lateinit var viewModel: GameFinishViewModel
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private val scoreboardRepository: ScoreboardRepository = mockk()

    private val testBoardSize = 8
    private val testTimeMillis = 10000L
    private val testMoves = 15
    private val testPlayerName = "TestPlayer"

    @BeforeEach
    fun setUp() {
        val gameFinish = GameFinish(
            boardSize = testBoardSize,
            timeMillis = testTimeMillis,
            moves = testMoves
        )
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<GameFinish>() } answers { gameFinish }

        viewModel = GameFinishViewModel(savedStateHandle, scoreboardRepository)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `gameFinish data is correctly initialized from SavedStateHandle`() {
        // Then
        assertEquals(testBoardSize, viewModel.gameFinish.boardSize)
        assertEquals(testTimeMillis, viewModel.gameFinish.timeMillis)
    }

    @Test
    fun `saveRecord calls scoreboardRepository with correct data`() {
        // Given
        val recordSlot = slot<ScoreboardRepository.ScoreboardRecord>()
        justRun { scoreboardRepository.saveScore(capture(recordSlot)) }

        // When
        viewModel.saveRecord(testPlayerName)

        // Then
        verify { scoreboardRepository.saveScore(any()) }
        val capturedRecord = recordSlot.captured
        assertEquals(testPlayerName, capturedRecord.player)
        assertEquals(testTimeMillis.milliseconds, capturedRecord.time)
        assertEquals(testBoardSize, capturedRecord.boardSize)
    }
}
