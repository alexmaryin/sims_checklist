package feature.metarscreen

import feature.metarscreen.model.ErrorUi
import feature.metarscreen.model.MetarUi

data class WindViewState(
    val data: MetarUi = MetarUi(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error: ErrorUi? = null,
    val showInfo: Boolean = false
)
