package ru.alexmaryin.simschecklist

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import com.arkivanov.decompose.defaultComponentContext
import decompose.Root

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val root = Root(defaultComponentContext())
        setContent {
            val isDark = isSystemInDarkTheme()
            MaterialTheme(colors = if (isDark) Themes.dark else Themes.light) {
                App(root)
            }
        }
    }
}
