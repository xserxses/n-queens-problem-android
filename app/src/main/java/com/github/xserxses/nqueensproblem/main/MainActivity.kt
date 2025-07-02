package com.github.xserxses.nqueensproblem.main

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github.xserxses.nqueensproblem.game.board.GameBoardScreen
import com.github.xserxses.nqueensproblem.scoreboard.ScoreboardScreen
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme
import com.github.xserxses.nqueensproblem.welcome.home.WelcomeHomeScreen
import com.github.xserxses.nqueensproblem.welcome.new.WelcomeHomeNew
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

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
                            WelcomeHomeScreen(
                                onNavigateNew = { navController.navigate(WelcomeNew) },
                                onNavigateContinue = { navController.navigate(GameBoard(GameBoardData.Continue)) },
                                onNavigateScoreboard = { navController.navigate(Scoreboard) }
                            )
                        }
                        dialog<WelcomeNew> {
                            WelcomeHomeNew(
                                onNavigateBoard = {
                                    navController.navigate(GameBoard(GameBoardData.New(6)))
                                }
                            )
                        }
                        val dataType = object : NavType<GameBoardData>(isNullableAllowed = false) {
                            override fun get(bundle: Bundle, key: String): GameBoardData? {
                                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    bundle.getParcelable(key, GameBoardData::class.java)
                                } else {
                                    @Suppress("DEPRECATION")
                                    bundle.getParcelable(key)
                                }
                            }

                            override fun parseValue(value: String): GameBoardData {
                                return Json.decodeFromString<GameBoardData>(value)
                            }

                            override fun serializeAsValue(value: GameBoardData): String {
                                return Uri.encode(Json.encodeToString(value))
                            }

                            override fun put(bundle: Bundle, key: String, value: GameBoardData) {
                                bundle.putParcelable(key, value)
                            }
                        }
                        composable<GameBoard>(
                            typeMap = mapOf(typeOf<GameBoardData>() to dataType)
                        ) { backStackEntry ->

                            val board: GameBoard = backStackEntry.toRoute()
                            GameBoardScreen(
                                variant = board.toString()
                            )
                        }
                        composable<Scoreboard> { ScoreboardScreen() }
                    }
                }
            }
        }
    }
}

@Serializable
object WelcomeHome

@Serializable
object WelcomeNew

@Serializable
data class GameBoard(val gameBoardData: GameBoardData)

@Serializable
@Parcelize
sealed interface GameBoardData : Parcelable {
    @Serializable
    object Continue : GameBoardData

    @Serializable
    data class New(val boardSize: Int) : GameBoardData
}

@Serializable
object Scoreboard
