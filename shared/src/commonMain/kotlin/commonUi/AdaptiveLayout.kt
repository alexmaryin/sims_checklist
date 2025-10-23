package commonUi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import commonUi.utils.DeviceConfiguration

@Composable
fun AdaptiveLayout(
    padding: PaddingValues,
    children: @Composable (width: Dp, height: Dp, isLandscape: Boolean) -> Unit
) {
    val layoutModifier = Modifier.fillMaxWidth().padding(8.dp)
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSize(windowSize)

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
            .padding(padding)
            .consumeWindowInsets(WindowInsets.safeDrawing)
    ) {
        val width = maxWidth
        val height = maxHeight
        when (deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT,
            DeviceConfiguration.TABLET_PORTRAIT -> {
                Column(
                    modifier = layoutModifier.verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    children(width, height, false)
                }
            }

            else -> {
                Row(modifier = layoutModifier) {
                    children(width, height, true)
                }
            }
        }
    }
}