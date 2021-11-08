package ru.alexmaryin.simschecklist

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.defaultComponentContext
import com.google.accompanist.insets.*
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import decompose.Root
import decompose.SimViewState
import feature.remote.service.MetarService

class MainActivity : AppCompatActivity() {

    private val viewModel: SimViewState by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = MetarService.create()
        val root = Root(defaultComponentContext(), viewModel.aircraftRepository, service)

        setContent {
            val isDark = isSystemInDarkTheme()
            MaterialTheme(colors = if(isDark) darkColors() else lightColors()) {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    App(root)
//                    Sample()
                }
            }
        }
    }
}

val listItems = List(40) { it }

@OptIn(ExperimentalAnimatedInsets::class)
@Composable
private fun Sample() {
    Scaffold(
        topBar = {
            // We use TopAppBar from accompanist-insets-ui which allows us to provide
            // content padding matching the system bars insets.
            TopAppBar(
                title = { Text("Title") },
                backgroundColor = MaterialTheme.colors.surface,
                contentPadding = rememberInsetsPaddingValues(
                    LocalWindowInsets.current.statusBars,
                    applyBottom = false,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        bottomBar = {
            Surface(elevation = 1.dp) {
                val text = remember { mutableStateOf(TextFieldValue()) }
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    placeholder = { Text(text = "Watch me animate...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .navigationBarsWithImePadding()
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        Column {
            // We apply the contentPadding passed to us from the Scaffold
            LazyColumn(
                contentPadding = contentPadding,
                reverseLayout = true,
                modifier = Modifier
                    .weight(1f)
                    .nestedScroll(connection = rememberImeNestedScrollConnection())
            ) {
                items(listItems) { item ->
                    Text(
                        text = "$item item",
                        modifier = Modifier.padding(16.dp).fillParentMaxWidth()
                    )
                }
            }
        }
    }
}


