package com.github.xserxses.nqueensproblem.scoreboard.ui

import kotlin.time.Duration

sealed class ScoreboardState {

    data object Loading : ScoreboardState()

    data object Empty : ScoreboardState()

    data class Scoreboard(
        val items: List<ScoreboardItem>
    ) : ScoreboardState() {
        data class ScoreboardItem(
            val player: String,
            val time: Duration
        )
    }

    data class Error(val message: String) : ScoreboardState()
}
