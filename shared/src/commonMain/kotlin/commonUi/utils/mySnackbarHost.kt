package commonUi.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable

fun mySnackbarHost(
    snackbarHostState: SnackbarHostState
) = @Composable{
    SnackbarHost(snackbarHostState) {
        Snackbar(
            snackbarData = it,
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            actionColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}