package com.github.xserxses.nqueensproblem.game.board.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardState
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme

@Composable
fun GameBoardErrorComposable(state: GameBoardState.Error) {
    Column {
        Text(
            text = stringResource(
                when (state.reason) {
                    GameBoardState.Error.Reason.INVALID_SIZE -> R.string.game_invalid_size
                    GameBoardState.Error.Reason.UNKNOWN -> R.string.game_unknown_error
                }
            )
        )
    }
}

@PreviewLightDark
@Preview(showBackground = true)
@Composable
fun GameBoardErrorComposablePreview() {
    NQueensProblemTheme {
        GameBoardErrorComposable(
            GameBoardState.Error(
                GameBoardState.Error.Reason.INVALID_SIZE
            )
        )
    }
}
