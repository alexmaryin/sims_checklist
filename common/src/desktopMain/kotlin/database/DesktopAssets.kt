package database

import androidx.compose.ui.res.useResource

actual fun loadAircraftJson(filename: String): String =
    useResource("raw/$filename") { it.bufferedReader().readText() }

