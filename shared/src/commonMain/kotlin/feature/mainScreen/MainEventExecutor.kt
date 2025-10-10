package feature.mainScreen

import com.arkivanov.decompose.value.Value

interface MainEventExecutor {
    operator fun invoke(event: MainScreenEvent)

    val state: Value<MainScreenViewState>
}