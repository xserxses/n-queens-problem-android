package com.github.xserxses.nqueensproblem.persistance

import javax.inject.Inject

class GameRepository @Inject constructor() {

    fun isGameSavedAvailable(): Boolean = false

    fun restoreSavedGame(): Nothing = TODO()
}
