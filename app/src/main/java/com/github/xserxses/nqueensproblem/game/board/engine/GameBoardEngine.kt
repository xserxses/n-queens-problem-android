package com.github.xserxses.nqueensproblem.game.board.engine

import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode
import com.github.xserxses.nqueensproblem.persistance.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import javax.inject.Singleton
import kotlin.concurrent.timer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class GameBoardEngine(
    private val repository: GameRepository,
    mode: GameBoardEngineStartMode
) {
    private val _state: MutableStateFlow<GameBoardEngineGame> = MutableStateFlow(
        when (mode) {
            GameBoardEngineStartMode.Continue -> repository.restoreSavedGame()
            is GameBoardEngineStartMode.New -> initialValue(boardSize = mode.boardSize)
        } as GameBoardEngineGame
    )

    val state: StateFlow<GameBoardEngineGame> = _state
    private var timer: Timer? = null

    fun startTimer() {
        timer = timer(
            period = 1.seconds.inWholeMilliseconds
        ) {
            _state.update { state ->
                state.copy(timeElapsed = state.timeElapsed + 1.seconds)
            }
        }
    }

    fun stopTimer() {
        timer?.cancel()
    }

    suspend fun cellTapped(x: Int, y: Int) {
        _state.update { state ->
//            state.copy(
//                board = state.board[x][y]
//            )
            state
        }
        updateDanger(x, y)
    }

    private suspend fun updateDanger(x: Int, y: Int) {
        // go though diagonals & lines and look fo other queens
    }

    suspend fun save() {
        repository.saveGame(state.value.toGameEntity())
    }

    private fun GameBoardEngineGame.toGameEntity(): GameRepository.GameEntity =
        GameRepository.GameEntity(
            timeElapsed = timeElapsed,
            boardSize = board.size,
            queensPlaced = emptyList(),
            moves = 0,
        )

    suspend fun resetGame() {
        _state.value = initialValue(_state.value.board.size)
    }

    companion object {
        private fun initialValue(boardSize: Int) = GameBoardEngineGame(
            status = GameBoardEngineGame.Status.IN_PROGRESS,
            reminingQueens = boardSize,
            board = Array(boardSize) {
                Array(boardSize) {
                    GameBoardEngineGame.Cell(false, false)
                }
            },
            timeElapsed = Duration.ZERO
        )
    }
}
