package com.github.xserxses.nqueensproblem.game.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.xserxses.nqueensproblem.game.board.ui.GameBoardViewModel
import com.github.xserxses.nqueensproblem.game.board.ui.model.GameBoardState

@Composable
fun GameBoardScreen(
    viewModel: GameBoardViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (state) {
            is GameBoardState.Loading -> CircularProgressIndicator()
            is GameBoardState.Game -> {
            }
        }
    }
}
