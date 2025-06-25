package com.github.xserxses.nqueensproblem.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.xserxses.nqueensproblem.app.QueensApplication
import com.github.xserxses.nqueensproblem.main.di.MainComponent
import com.github.xserxses.nqueensproblem.ui.theme.NQueensProblemTheme

class MainActivity : ComponentActivity() {

    lateinit var component: MainComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        component = (applicationContext as QueensApplication)
            .appComponent
            .mainComponent()
            .create()

        component.inject(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NQueensProblemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NQueensProblemTheme {
        Greeting("Android")
    }
}
