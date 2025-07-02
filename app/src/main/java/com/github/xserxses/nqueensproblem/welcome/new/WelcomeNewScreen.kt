package com.github.xserxses.nqueensproblem.welcome.new

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeHomeNew(
    onNavigateBoard: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = {}) {
        Button(
            onClick = { onNavigateBoard() }
        ) {
            Text("Start")
        }
    }
}
