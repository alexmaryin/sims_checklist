package commonUi

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import commonUi.utils.SimColors
import org.jetbrains.compose.resources.painterResource
import sims_checklist.shared.generated.resources.Res
import sims_checklist.shared.generated.resources.arrow_back
import sims_checklist.shared.generated.resources.remove_done

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithClearAction(caption: String, onBack: () -> Unit, onClear: () -> Unit) = TopAppBar(
    title = { Text(caption) },
    navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(painter = painterResource(Res.drawable.arrow_back), contentDescription = "Back button")
        }
    },
    actions = {
        IconButton(onClick = onClear) {
            Icon(painter = painterResource(Res.drawable.remove_done), contentDescription = "Uncheck all")
        }
    },
    colors = SimColors.topBarColors()
)

@Composable
expect fun Dialog(onDismissRequest: () -> Unit, title: String, text: String)
