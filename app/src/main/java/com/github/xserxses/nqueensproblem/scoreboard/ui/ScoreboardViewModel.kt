package com.github.xserxses.nqueensproblem.scoreboard.ui

import androidx.lifecycle.ViewModel
import com.github.xserxses.nqueensproblem.persistance.ScoreboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScoreboardViewModel @Inject constructor(
    private val scoreboardRepository: ScoreboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ScoreboardState>(ScoreboardState.Loading)
    val state: StateFlow<ScoreboardState> = _state

    fun onSizeChanged(size: Int) {
        try {
            _state.value = ScoreboardState.Loading
            val items = scoreboardRepository
                .getScoreboardForBoardSize(size)
                .map { ScoreboardState.Scoreboard.ScoreboardItem(it.player, it.time) }

            _state.value = if (items.isEmpty()) {
                ScoreboardState.Empty
            } else {
                ScoreboardState.Scoreboard(items)
            }
        } catch (e: Exception) {
            _state.value = ScoreboardState.Error(e.message ?: "Unknown error")
        }
    }
}
