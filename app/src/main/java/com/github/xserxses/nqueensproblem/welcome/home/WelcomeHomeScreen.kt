package com.github.xserxses.nqueensproblem.welcome.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.xserxses.nqueensproblem.R
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun WelcomeHomeScreen(
    viewModel: WelcomeHomeViewModel = hiltViewModel(),
    onNavigateContinue: () -> Unit,
    onNavigateNew: () -> Unit,
    onNavigateScoreboard: (boardSize: Int?) -> Unit
) {
    WelcomeHomeScreenContent(
        continueEnabled = viewModel
            .state
            .collectAsState()
            .value
            .continueEnabled,
        onNavigateContinue = onNavigateContinue,
        onNavigateNew = onNavigateNew,
        onNavigateScoreboard = onNavigateScoreboard
    )
}

@Composable
private fun WelcomeHomeScreenContent(
    continueEnabled: Boolean,
    onNavigateContinue: () -> Unit,
    onNavigateNew: () -> Unit,
    onNavigateScoreboard: (boardSize: Int?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to N-Queens Problem!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
        Image(
            modifier = Modifier
                .padding(all = 32.dp),
            painter = painterResource(id = R.drawable.welcome_screen_board),
            contentDescription = stringResource(id = R.string.welcome_home_board_content_desc)
        )
        WelcomeHomeButton(
            text = "Continue",
            enabled = continueEnabled,
            onClick = { onNavigateContinue() }
        )
        WelcomeHomeButton(
            text = "New game",
            onClick = { onNavigateNew() }
        )
        WelcomeHomeButton(
            text = "Scoreboard",
            onClick = { onNavigateScoreboard(null) }
        )
    }
}

@Composable
private fun WelcomeHomeButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth(0.66f),
        onClick = { onClick() }
    ) {
        Text(text)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@PreviewLightDark
@Composable
private fun WelcomeHomeScreenContentPreview() {
    NQueensProblemTheme {
        WelcomeHomeScreenContent(
            continueEnabled = false,
            onNavigateContinue = {},
            onNavigateNew = {},
            onNavigateScoreboard = {}
        )
    }
}
