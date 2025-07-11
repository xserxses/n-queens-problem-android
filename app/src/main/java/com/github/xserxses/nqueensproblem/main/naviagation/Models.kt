package com.github.xserxses.nqueensproblem.main.naviagation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
object WelcomeHome

@Serializable
object WelcomeNew

@Serializable
data class GameBoard(val gameBoardArgs: GameBoardArgs)

@Serializable
@Parcelize
sealed interface GameBoardArgs : Parcelable {

    companion object {
        val dataType = object : NavType<GameBoardArgs>(isNullableAllowed = false) {
            override fun get(bundle: Bundle, key: String): GameBoardArgs? {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(key, GameBoardArgs::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    bundle.getParcelable(key)
                }
            }

            override fun parseValue(value: String): GameBoardArgs {
                return Json.decodeFromString<GameBoardArgs>(value)
            }

            override fun serializeAsValue(value: GameBoardArgs): String {
                return Uri.encode(Json.encodeToString(value))
            }

            override fun put(bundle: Bundle, key: String, value: GameBoardArgs) {
                bundle.putParcelable(key, value)
            }
        }
    }

    @Serializable
    object Continue : GameBoardArgs

    @Serializable
    data class New(val boardSize: Int) : GameBoardArgs
}

@Serializable
object Scoreboard
