package com.github.xserxses.nqueensproblem.welcome.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WelcomeHomeScreen(
    viewModel: WelcomeHomeViewModel = viewModel(),
    onNavigateToBoard: () -> Unit,
) {
    Column {
        Text(
            text = "Welcome ${viewModel.user}",
            modifier = Modifier
        )
        Button(onClick = { onNavigateToBoard() }) {
            Text("Go to Board")
        }
    }
}
