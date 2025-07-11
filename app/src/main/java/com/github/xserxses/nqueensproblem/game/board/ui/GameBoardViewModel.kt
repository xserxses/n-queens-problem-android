package com.github.xserxses.nqueensproblem.game.board.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.xserxses.nqueensproblem.game.board.engine.GameBoardEngine
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode.Continue
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode.New
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardElementUi
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardState
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoard
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoardArgs
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoardArgs.Companion.dataType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

@HiltViewModel
class GameBoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val engine: GameBoardEngine
) : ViewModel() {
    private val gameBoardRoute: GameBoard = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<GameBoardArgs>() to dataType)
    )

    private val boardArgs: GameBoardArgs = gameBoardRoute.gameBoardArgs

    private val _state: MutableStateFlow<GameBoardState> = MutableStateFlow(GameBoardState.Loading)
    val state: StateFlow<GameBoardState> = _state

    init {
        viewModelScope.launch {
            engine.start(
                when (boardArgs) {
                    is GameBoardArgs.Continue -> Continue
                    is GameBoardArgs.New -> New(boardArgs.boardSize)
                }
            )
        }

        engine
            .state
            .map { mapForUi(it) }
            .map { addBorders(it) }
            .distinctUntilChanged()
            .onEach { _state.value = it }
            .launchIn(viewModelScope)
    }

    private fun mapForUi(game: GameBoardEngineGame): GameBoardState {
        TODO()
    }

    private fun addBorders(it: GameBoardState): GameBoardState {
        TODO("Not yet implemented")
    }

    fun onCellTapped(cell: GameBoardElementUi.Cell) {
        viewModelScope.launch {
            engine.cellTapped(cell.x, cell.y)
        }
    }

    fun onResetTapped() {
        viewModelScope.launch {
            engine.resetGame()
        }
    }

    fun pauseGame() {
        engine.stopTimer()
    }

    fun resumeGame() {
        engine.startTimer()
    }
}
