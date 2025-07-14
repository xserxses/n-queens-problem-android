package com.github.xserxses.nqueensproblem.game.finish

import androidx.lifecycle.ViewModel
import com.github.xserxses.nqueensproblem.persistance.ScoreboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class GameFinishViewModel @Inject constructor(
//    private val gameBoardEngine: GameBoardEngine,
    private val scoreboardRepository: ScoreboardRepository
) : ViewModel() {

    fun saveRecord(name: String?) {
//        val entity = ScoreboardRepository.ScoreboardRecord(
//            player = name ?: UUID.randomUUID().toString(),
//            time = gameBoardEngine.state.value.timeElapsed,
//            boardSize = gameBoardEngine.state.value.board.size
//        )
//
//        scoreboardRepository.saveScore(entity)
    }
}
