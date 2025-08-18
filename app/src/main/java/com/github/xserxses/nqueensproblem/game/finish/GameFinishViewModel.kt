package com.github.xserxses.nqueensproblem.game.finish

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.github.xserxses.nqueensproblem.main.naviagation.GameFinish
import com.github.xserxses.nqueensproblem.persistance.ScoreboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameFinishViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val scoreboardRepository: ScoreboardRepository
) : ViewModel() {

    val gameFinish: GameFinish = savedStateHandle.toRoute()

    fun saveRecord(name: String) {
        val entity = ScoreboardRepository.ScoreboardRecord(
            player = name,
            time = gameFinish.timeMillis.milliseconds,
            boardSize = gameFinish.boardSize
        )

        scoreboardRepository.saveScore(entity)
    }
}
