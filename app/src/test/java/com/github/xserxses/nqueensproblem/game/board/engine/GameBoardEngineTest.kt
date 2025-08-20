
import com.github.xserxses.nqueensproblem.game.board.engine.GameBoardEngine
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode
import com.github.xserxses.nqueensproblem.persistance.GameRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

@ExperimentalCoroutinesApi
class GameBoardEngineTest {

    private lateinit var gameBoardEngine: GameBoardEngine
    private lateinit var mockRepository: GameRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state for new game is correct`() = runTest(testDispatcher) {
        val boardSize = 4
        gameBoardEngine = GameBoardEngine(mockRepository, GameBoardEngineStartMode.New(boardSize))

        val initialState = gameBoardEngine.state.first()

        assertEquals(GameBoardEngineGame.Status.IN_PROGRESS, initialState.status)
        assertTrue(initialState.queens.isEmpty())
        assertTrue(initialState.dangerCells.isEmpty())
        assertEquals(boardSize, initialState.boardSize)
        assertEquals(0, initialState.moves)
    }

    @Test
    fun `initial state for continue game restores from repository`() = runTest(testDispatcher) {
        val boardSize = 8
        val savedQueens = listOf(GameRepository.Coordinates(0, 0))
        val savedGameEntity = GameRepository.GameEntity(
            boardSize = boardSize,
            moves = 5,
            queensPlaced = savedQueens,
            timeElapsed = 10.seconds
        )
        every { mockRepository.restoreSavedGame() } returns savedGameEntity

        gameBoardEngine = GameBoardEngine(mockRepository, GameBoardEngineStartMode.Continue)
        testScheduler.advanceUntilIdle()

        val restoredState = gameBoardEngine.state.first()

        assertEquals(GameBoardEngineGame.Status.IN_PROGRESS, restoredState.status)
        assertEquals(1, restoredState.queens.size)
        assertEquals(GameBoardEngineGame.Coordinates(0, 0), restoredState.queens.first())

        assertTrue(restoredState.dangerCells.isEmpty())
        assertEquals(boardSize, restoredState.boardSize)
        assertEquals(5, restoredState.moves)
    }

    @Test
    fun `initial state for continue game with no saved data creates new game`() =
        runTest(testDispatcher) {
            every { mockRepository.restoreSavedGame() } returns null

            gameBoardEngine = GameBoardEngine(mockRepository, GameBoardEngineStartMode.Continue)
            testScheduler.advanceUntilIdle()

            val initialState = gameBoardEngine.state.first()

            assertEquals(GameBoardEngineGame.Status.IN_PROGRESS, initialState.status)
            assertTrue(initialState.queens.isEmpty())
            assertTrue(initialState.dangerCells.isEmpty())
            assertEquals(8, initialState.boardSize) // Default board size
            assertEquals(0, initialState.moves)
        }

    @Test
    fun `cellTapped adds queen to empty cell`() = runTest(testDispatcher) {
        gameBoardEngine = GameBoardEngine(mockRepository, GameBoardEngineStartMode.New(4))
        testScheduler.advanceUntilIdle()

        gameBoardEngine.cellTapped(0, 0)
        testScheduler.advanceUntilIdle()

        val updatedState = gameBoardEngine.state.first()
        assertEquals(1, updatedState.queens.size)
        assertEquals(GameBoardEngineGame.Coordinates(0, 0), updatedState.queens.first())
        assertEquals(1, updatedState.moves)
    }
}
