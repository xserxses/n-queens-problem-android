package com.github.xserxses.nqueensproblem.welcome.new

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WelcomeHomeNew(
    onNavigateBoard: () -> Unit
) {
    Text(
        text = "WelcomeHomeNew",
        modifier = Modifier
    )
}
