package com.github.xserxses.nqueensproblem.game.board.engine

import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame.Coordinates
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame.Status
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode
import com.github.xserxses.nqueensproblem.persistance.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Singleton
import kotlin.math.abs
import kotlin.time.Duration

@Singleton
class GameBoardEngine(
    private val repository: GameRepository,
    mode: GameBoardEngineStartMode
) {
    private val engineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _state: MutableStateFlow<GameBoardEngineGame> = MutableStateFlow(
        when (mode) {
            GameBoardEngineStartMode.Continue ->
                repository
                    .restoreSavedGame()
                    ?.toGameBoardEngineGame() ?: initialValue(boardSize = 8)

            is GameBoardEngineStartMode.New -> initialValue(boardSize = mode.boardSize)
        }
    )

    val state: StateFlow<GameBoardEngineGame> = _state

    fun cellTapped(x: Int, y: Int) {
        engineScope.launch(Dispatchers.Default) {
            _state.update { game ->
                handleCellTap(
                    currentGame = game,
                    x = x,
                    y = y
                )
            }
        }
    }

    fun handleCellTap(currentGame: GameBoardEngineGame, x: Int, y: Int): GameBoardEngineGame {
        val tappedCoordinates = Coordinates(x, y)
        val queensCopy = _state.value.queens.toMutableList()
        // Add or remove the queen at the tapped coordinates
        val queenExists = queensCopy.contains(tappedCoordinates)
        if (queenExists) {
            queensCopy.remove(tappedCoordinates)
        } else {
            queensCopy.add(tappedCoordinates)
        }

        // Recalculate which queens are in danger
        val dangerCells = calculateDangerCells(queensCopy)

        val status = if (queensCopy.size == currentGame.boardSize && dangerCells.isEmpty()) {
            Status.FINISHED
        } else {
            Status.IN_PROGRESS
        }

        return currentGame.copy(
            queens = queensCopy,
            dangerCells = dangerCells,
            moves = currentGame.moves + 1,
            status = status
        )
    }

    private fun calculateDangerCells(queens: List<Coordinates>): List<Coordinates> {
        val dangerPathCells = mutableSetOf<Coordinates>()

        if (queens.size < 2) {
            return emptyList()
        }

        // Iterate through all unique pairs of queens to check for threats.
        for (i in 0 until queens.size - 1) {
            for (j in i + 1 until queens.size) {
                val queen1 = queens[i]
                val queen2 = queens[j]

                // Check for horizontal threat (same row)
                if (queen1.x == queen2.x) {
                    val startY = minOf(queen1.y, queen2.y)
                    val endY = maxOf(queen1.y, queen2.y)
                    // Add all cells on the horizontal path between the queens.
                    for (y in startY..endY) {
                        dangerPathCells.add(Coordinates(queen1.x, y))
                    }
                }
                // Check for vertical threat (same column)
                else if (queen1.y == queen2.y) {
                    val startX = minOf(queen1.x, queen2.x)
                    val endX = maxOf(queen1.x, queen2.x)
                    // Add all cells on the vertical path between the queens.
                    for (x in startX..endX) {
                        dangerPathCells.add(Coordinates(x, queen1.y))
                    }
                }
                // Check for diagonal threat
                else if (abs(queen1.x - queen2.x) == abs(queen1.y - queen2.y)) {
                    // Determine the direction of the diagonal path (e.g., +1, -1)
                    val xDirection = if (queen2.x > queen1.x) 1 else -1
                    val yDirection = if (queen2.y > queen1.y) 1 else -1

                    var currentX = queen1.x
                    var currentY = queen1.y

                    while (currentX != queen2.x) {
                        dangerPathCells.add(Coordinates(currentX, currentY))
                        currentX += xDirection
                        currentY += yDirection
                    }
                    dangerPathCells.add(queen2)
                }
            }
        }
        return dangerPathCells.toList()
    }

    fun save(timeElapsed: Duration) {
        repository.saveGame(state.value.toGameEntity(timeElapsed))
    }

    private fun GameBoardEngineGame.toGameEntity(timeElapsed: Duration): GameRepository.GameEntity {
        val queens = queens.map {
            GameRepository.Coordinates(it.x, it.y)
        }

        return GameRepository.GameEntity(
            boardSize = _state.value.boardSize,
            moves = moves,
            queensPlaced = queens,
            timeElapsed = timeElapsed
        )
    }

    fun resetGame() {
        _state.value = initialValue(_state.value.boardSize)
    }

    private fun GameRepository.GameEntity.toGameBoardEngineGame(): GameBoardEngineGame {
        val queens = queensPlaced.map { Coordinates(it.x, it.y) }
        return GameBoardEngineGame(
            status = Status.IN_PROGRESS,
            queens = queens,
            dangerCells = calculateDangerCells(queens),
            boardSize = boardSize,
            moves = moves
        )
    }

    companion object {
        private const val TAG = "GameBoardEngine"
        private fun initialValue(boardSize: Int) = GameBoardEngineGame(
            status = Status.IN_PROGRESS,
            queens = emptyList(),
            dangerCells = emptyList(),
            boardSize = boardSize,
            moves = 0
        )
    }
}
