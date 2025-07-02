package com.github.xserxses.nqueensproblem.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.xserxses.nqueensproblem.game.board.GameBoardScreen
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import com.github.xserxses.nqueensproblem.welcome.home.WelcomeHomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NQueensProblemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = WelcomeHome,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<WelcomeHome> {
                            WelcomeHomeScreen {
                                navController.navigate(GameBoard)
                            }
                        }
                        composable<GameBoard> { GameBoardScreen() }
                    }
                }
            }
        }
    }
}

@Serializable
object WelcomeHome

@Serializable
object GameBoard
