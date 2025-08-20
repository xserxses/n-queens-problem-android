package com.github.xserxses.nqueensproblem.game.board.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.xserxses.nqueensproblem.game.board.engine.GameBoardEngine
import com.github.xserxses.nqueensproblem.game.board.engine.GameBoardEngineFactory
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame.Coordinates
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineGame.Status
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode.Continue
import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode.New
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardElementUi
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardState
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardUi
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoard
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoardArgs
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoardArgs.Companion.dataType
import com.github.xserxses.nqueensproblem.utils.getExcelColumnName
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.Timer
import kotlin.concurrent.timer
import kotlin.reflect.typeOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class GameBoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val engineFactory: GameBoardEngineFactory
) : ViewModel() {
    private val gameBoardRoute: GameBoard = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<GameBoardArgs>() to dataType)
    )

    private val boardArgs: GameBoardArgs = gameBoardRoute.gameBoardArgs

    private val _state: MutableStateFlow<GameBoardState> = MutableStateFlow(GameBoardState.Loading)
    val state: StateFlow<GameBoardState> = _state

    private val engine: GameBoardEngine = when (boardArgs) {
        is GameBoardArgs.Continue -> engineFactory.create(Continue)
        is GameBoardArgs.New -> engineFactory.create(New(boardArgs.boardSize))
    }

    private var gameState: GameBoardState.Game.STATE = GameBoardState.Game.STATE.PLAYING

    private var timer: Timer? = null
    private var timeElapsed = 0.seconds

    fun startTimer() {
        timer?.cancel()
        timer =
            timer(
                name = TIMER_NAME,
                daemon = true,
                period = 1.seconds.inWholeMilliseconds
            ) {
                _state.update {
                    timeElapsed = timeElapsed + 1.seconds
                    if (it is GameBoardState.Game) {
                        if (it.state == GameBoardState.Game.STATE.PLAYING) {
                            viewModelScope.launch(Dispatchers.IO) {
                                engine.save(timeElapsed)
                            }
                        }
                        it.copy(time = timeElapsed)
                    } else {
                        it
                    }
                }
            }
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private val listWithBorders: List<GameBoardElementUi> by lazy {
        val uiElements = mutableListOf<GameBoardElementUi>()
        val boardSize = engine.state.value.boardSize

        uiElements.add(GameBoardElementUi.Placeholder(x = 0, y = 0))
        uiElements.add(GameBoardElementUi.Placeholder(x = 0, y = boardSize + 1))
        uiElements.add(GameBoardElementUi.Placeholder(x = boardSize + 1, y = 0))
        uiElements.add(GameBoardElementUi.Placeholder(x = boardSize + 1, y = boardSize + 1))

        for (i in 0 until boardSize) {
            uiElements.add(
                GameBoardElementUi.Border(
                    text = getExcelColumnName(i),
                    x = i + 1,
                    y = 0
                )
            )
            uiElements.add(
                GameBoardElementUi.Border(
                    text = getExcelColumnName(i),
                    x = i + 1,
                    y = boardSize + 1
                )
            )
            uiElements.add(
                GameBoardElementUi.Border(
                    text = (i + 1).toString(),
                    x = 0,
                    y = i + 1
                )
            )
            uiElements.add(
                GameBoardElementUi.Border(
                    text = (i + 1).toString(),
                    x = boardSize + 1,
                    y = i + 1
                )
            )
        }
        uiElements
    }

    init {
        startTimer()
        engine
            .state
            .map { mapEngineGameToUiStateWithBorders(it) }
            .distinctUntilChanged()
            .onEach {
                _state.value = it
            }
            .launchIn(viewModelScope + Dispatchers.Default)
    }

    private fun mapEngineGameToUiStateWithBorders(game: GameBoardEngineGame): GameBoardState {
        val boardSize = game.boardSize
        if (boardSize < 4) {
            return GameBoardState.Error(GameBoardState.Error.Reason.INVALID_SIZE)
        }

        val uiElements = mutableListOf<GameBoardElementUi>()
            .apply {
                addAll(listWithBorders)
            }

        for (yIndex in 0 until boardSize) {
            for (xIndex in 0 until boardSize) {
                val cellColor = if ((xIndex + yIndex) % 2 == 0) {
                    GameBoardElementUi.Cell.CellColor.LIGHT
                } else {
                    GameBoardElementUi.Cell.CellColor.DARK
                }

                val hasQueen = engine.state.value.queens.contains(Coordinates(xIndex, yIndex))
                val cellState = when {
                    engine.state.value.dangerCells.contains(Coordinates(xIndex, yIndex))
                    -> GameBoardElementUi.Cell.CellState.DANGER

                    else -> GameBoardElementUi.Cell.CellState.NORMAL
                }

                uiElements.add(
                    GameBoardElementUi.Cell(
                        x = xIndex + 1, // UI coordinate, matches border
                        y = yIndex + 1, // UI coordinate, matches border
                        hasQueen = hasQueen,
                        state = cellState,
                        color = cellColor
                    )
                )
            }
        }

        val gameBoardUi = GameBoardUi(cells = uiElements)

        val currentUiGameState = when (game.status) {
            Status.IN_PROGRESS -> gameState
            Status.FINISHED -> {
                stopTimer()
                GameBoardState.Game.STATE.FINISHED
            }
        }

        return GameBoardState.Game(
            state = currentUiGameState,
            time = timeElapsed,
            moves = game.moves,
            remainingQueens = boardSize - game.queens.size,
            ui = gameBoardUi
        )
    }

    fun onCellTapped(cell: GameBoardElementUi.Cell) {
        engine.cellTapped(cell.x - 1, cell.y - 1) // -1 due to the border
    }

    fun onResetTapped() {
        viewModelScope.launch {
            engine.resetGame()
            timeElapsed = Duration.ZERO
        }
    }

    fun onBackgrounded() {
        (state.value as? GameBoardState.Game?)?.let {
            if (it.state in listOf(
                    GameBoardState.Game.STATE.PLAYING,
                    GameBoardState.Game.STATE.BACKGROUND
                )
            ) {
                stopTimer()
                gameState = GameBoardState.Game.STATE.BACKGROUND
                _state.update { state ->
                    (state as? GameBoardState.Game)?.copy(state = GameBoardState.Game.STATE.BACKGROUND)
                        ?: state
                }
            }
        }
    }

    fun onForegrounded() {
        (state.value as? GameBoardState.Game?)?.let {
            if (it.state == GameBoardState.Game.STATE.BACKGROUND) {
                startTimer()
                gameState = GameBoardState.Game.STATE.PLAYING
                _state.update { state ->
                    (state as? GameBoardState.Game)?.copy(state = GameBoardState.Game.STATE.PLAYING)
                        ?: state
                }
            }
        }
    }

    fun onPlayPauseClick() {
        val game = state.value as? GameBoardState.Game
        game?.let {
            when (it.state) {
                GameBoardState.Game.STATE.PLAYING -> {
                    stopTimer()
                    _state.update { state ->
                        (state as? GameBoardState.Game)
                            ?.copy(state = GameBoardState.Game.STATE.PAUSED) ?: state
                    }
                }

                GameBoardState.Game.STATE.PAUSED -> {
                    startTimer()
                    _state.update { state ->
                        (state as? GameBoardState.Game)
                            ?.copy(state = GameBoardState.Game.STATE.PLAYING) ?: state
                    }
                }

                else -> {}
            }
        }
    }

    companion object {
        private const val TIMER_NAME = "GameBoardTimer"
        private const val TAG = "GameBoardViewModel"
    }
}
