package decompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.Database
import model.Item
import ui.ItemListScreen

class ItemList(
    database: Database,
    val onItemSelected: (id: Long) -> Unit
) {
    private val _state = mutableStateOf(database.getAll())
    val state: State<List<Item>> get() = _state
}

@Composable
fun ItemListUi(list: ItemList) {
    ItemListScreen(
        items = list.state.value,
        onItemClick = list.onItemSelected
    )
}