package com.github.xserxses.nqueensproblem.game.board.engine

import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode
import com.github.xserxses.nqueensproblem.persistance.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import java.util.Timer
import javax.inject.Inject

class GameBoardEngine @Inject constructor(
    private val repository: GameRepository
) {

    private val _state: MutableStateFlow<GameBoardEngineGame?> = MutableStateFlow(null)
    val state: Flow<GameBoardEngineGame> = _state.filterNotNull()

    private val timer : Timer? = null

    suspend fun start(mode: GameBoardEngineStartMode) {
        when (mode) {
            GameBoardEngineStartMode.Continue -> repository.restoreSavedGame()
            is GameBoardEngineStartMode.New -> initGame(boardSize = mode.boardSize)
        }
    }

    private fun initGame(boardSize: Int): Nothing {
        TODO("Not yet implemented")
    }

    fun startTimer() {
    }

    fun stopTimer() {

    }

    suspend fun cellTapped(x: Int, y: Int) {
    }

    suspend fun save() {
    }

    suspend fun resetGame() {
    }
}
