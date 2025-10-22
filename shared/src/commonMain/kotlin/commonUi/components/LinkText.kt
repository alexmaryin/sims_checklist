package commonUi.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration

/**
 * A composable that displays text styled as a hyperlink and opens the given [url] when clicked.
 *
 * @param text The text to display.
 * @param url The URL to open in the browser when the text is clicked.
 * @param modifier The modifier to be applied to the text.
 */
@Composable
fun LinkText(text: String, url: String, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Text(
        text = text,
        modifier = modifier.clickable {
            uriHandler.openUri(url)
        },
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline
    )
}