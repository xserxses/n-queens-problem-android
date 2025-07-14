package com.github.xserxses.nqueensproblem.game.board.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardState

@Composable
fun GameBoardErrorComposable(state: GameBoardState.Error) {
    Column() {
        Text(
            text = state.message ?: "Unknown error"
        )
    }
}

@PreviewLightDark
@Composable
fun GameBoardErrorComposablePreview() {
    GameBoardErrorComposable(
        GameBoardState.Error(
            GameBoardState.Error.ErrorReason.NO_SAVED_GAME_FOUND,
            "No saved game found, start new game."
        )
    )
}
