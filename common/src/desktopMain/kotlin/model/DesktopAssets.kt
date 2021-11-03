package model

import androidx.compose.ui.res.useResource

actual fun loadAircraftJson(filename: String): String = useResource(filename) { it.bufferedReader().readText() }

