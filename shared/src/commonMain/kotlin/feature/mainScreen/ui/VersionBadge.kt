package feature.mainScreen.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.BuildKonfig
import commonUi.utils.SimColors
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.airplanes

@Composable
fun VersionBadge() = BadgedBox(
    modifier = Modifier.padding(end = 20.dp),
    badge = {
        Badge(
            containerColor = SimColors.accentGreen,
            contentColor = SimColors.textLight,
        ) { Text(BuildKonfig.VER) }
    }
) {
    Icon(
        painter = painterResource(Res.drawable.airplanes),
        contentDescription = "app version"
    )
}