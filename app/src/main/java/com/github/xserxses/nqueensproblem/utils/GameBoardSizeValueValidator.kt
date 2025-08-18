package com.github.xserxses.nqueensproblem.utils

object GameBoardSizeValueValidator : (Int) -> Boolean {
    override fun invoke(number: Int): Boolean {
        val isValid = number in 4..Int.MAX_VALUE
        return isValid
    }
}
