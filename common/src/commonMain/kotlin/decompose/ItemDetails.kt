package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.Database
import model.Item
import ui.ItemDetailsScreen

class ItemDetails(
    id: Long,
    database: Database,
    val onFinished: () -> Unit
) {
    private val _state = mutableStateOf(database.getById(id))
    val state: State<Item> get() = _state
}

@Composable
fun ItemDetailsUi(details: ItemDetails) {
    ItemDetailsScreen(
        item = details.state.value,
        onBackClick = details.onFinished
    )
}