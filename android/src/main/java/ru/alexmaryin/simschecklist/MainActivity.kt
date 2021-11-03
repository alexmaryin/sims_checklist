package ru.alexmaryin.simschecklist

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import com.arkivanov.decompose.defaultComponentContext
import decompose.Root
import viewState.SimViewState

class MainActivity : AppCompatActivity() {

    private val viewModel: SimViewState by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = Root(defaultComponentContext(), viewModel.aircraftBase)

        setContent {
            val isDark = isSystemInDarkTheme()
            MaterialTheme(colors = if(isDark) darkColors() else lightColors()) {
                App(root)
            }
        }
    }
}

