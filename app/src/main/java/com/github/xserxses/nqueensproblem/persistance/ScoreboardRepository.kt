package com.github.xserxses.nqueensproblem.persistance

import kotlin.time.Duration

class ScoreboardRepository {

    data class ScoreboardRecord(
        val player: String,
        val time: Duration,
        val boardSize: Int,
    )

    fun getScoreboardForBoardSize() : List<ScoreboardRecord>{
        return listOf()
    }

    fun saveScore(score: ScoreboardRecord){

    }
}
