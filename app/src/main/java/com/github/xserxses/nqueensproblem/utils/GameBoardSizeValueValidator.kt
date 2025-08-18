package com.github.xserxses.nqueensproblem.utils

import android.util.Log

object GameBoardSizeValueValidator : (Int) -> Boolean {
    override fun invoke(number: Int): Boolean {
        val isValid = number in 4..Int.MAX_VALUE
        Log.d("GameBoardSizeValueValidator", "$number isValid: $isValid")
        return isValid
    }
}
