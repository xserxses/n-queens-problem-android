package com.github.xserxses.nqueensproblem.utils

class GameBoardSizeValueValidator : (Int) -> Boolean {
    override fun invoke(number: Int): Boolean =
        number in 4..Int.MAX_VALUE
}
