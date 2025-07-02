package com.github.xserxses.nqueensproblem.game.board

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GameBoardScreen(variant: String) {
    Text(
        text = "GameBoardScreen - $variant",
        modifier = Modifier
    )
}
