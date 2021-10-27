package ru.alexmaryin.simschecklist

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.essenty.backpressed.BackPressedHandler
import decompose.localBackPressedDispatcher

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val backPressedDispatcher = BackPressedHandler(onBackPressedDispatcher)

        setContent {
            CompositionLocalProvider(localBackPressedDispatcher provides backPressedDispatcher ) {
                App()
            }
        }
    }
}
