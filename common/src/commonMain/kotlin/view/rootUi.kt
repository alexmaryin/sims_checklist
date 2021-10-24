package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import decompose.Root

@Composable
fun RootUi(component: Root) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = component::onPrevious
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "previous checklist"
                )
            }
            Text(modifier = Modifier.weight(1f), text = "")
            IconButton(
                onClick = component::onNext
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "next checklist"
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Children(
                routerState = component.routerState,
                animation = slide()
            ) {
                when(val child = it.instance) {
                    is Root.Child.Page -> PageUi(child.component, Modifier.fillMaxSize())
                }
            }
        }
    }
}