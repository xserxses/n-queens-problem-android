package com.github.xserxses.nqueensproblem.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.xserxses.nqueensproblem.game.board.GameBoardScreen
import com.github.xserxses.nqueensproblem.game.finish.GameFinishScreen
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoard
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoardArgs
import com.github.xserxses.nqueensproblem.main.naviagation.GameBoardArgs.Companion.dataType
import com.github.xserxses.nqueensproblem.main.naviagation.GameFinish
import com.github.xserxses.nqueensproblem.main.naviagation.Scoreboard
import com.github.xserxses.nqueensproblem.main.naviagation.WelcomeHome
import com.github.xserxses.nqueensproblem.main.naviagation.WelcomeNew
import com.github.xserxses.nqueensproblem.scoreboard.ScoreboardScreen
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import com.github.xserxses.nqueensproblem.welcome.home.WelcomeHomeScreen
import com.github.xserxses.nqueensproblem.welcome.new.WelcomeHomeNew
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
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
                            WelcomeHomeScreen(
                                onNavigateNew = { navController.navigate(WelcomeNew) },
                                onNavigateContinue = {
                                    navController.navigate(
                                        GameBoard(
                                            GameBoardArgs.Continue
                                        )
                                    )
                                },
                                onNavigateScoreboard = {
                                    navController
                                        .navigate(Scoreboard(it))
                                }
                            )
                        }
                        dialog<WelcomeNew> {
                            WelcomeHomeNew(
                                onNavigateBoard = { boardSize ->
                                    navController.navigate(GameBoard(GameBoardArgs.New(boardSize)))
                                },
                                onDismiss = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable<GameBoard>(typeMap = mapOf(typeOf<GameBoardArgs>() to dataType)) { backStackEntry ->
                            GameBoardScreen()
                        }
                        dialog<GameFinish> {
                            GameFinishScreen(
                                onNavigateScoreboard = {
                                    navController
                                        .navigate(Scoreboard(it)) {
                                            popUpTo(WelcomeHome)
                                        }
                                },
                                onNavigateMainMenu = {
                                    navController.popBackStack<WelcomeHome>(inclusive = false)
                                },
                                onDismiss = { navController.popBackStack() }
                            )
                        }
                        composable<Scoreboard>(
                            typeMap = mapOf(typeOf<Scoreboard>() to Scoreboard.dataType)
                        ) { backStackEntry ->
                            val args: Scoreboard = backStackEntry.toRoute()
                            ScoreboardScreen(
                                args.boardSize
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
