package com.github.xserxses.nqueensproblem.game.board.engine

import com.github.xserxses.nqueensproblem.game.board.engine.model.GameBoardEngineStartMode
import com.github.xserxses.nqueensproblem.persistance.GameRepository
import jakarta.inject.Inject

class GameBoardEngineFactory @Inject constructor(
    private val repository: GameRepository
) {

    fun create(mode: GameBoardEngineStartMode): GameBoardEngine =
        GameBoardEngine(repository, mode)
}
