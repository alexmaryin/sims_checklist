package services.airportService.updateService

sealed class DownloadResult {
    data object Success : DownloadResult()
    data class Error(val message: String, val error: Exception? = null) : DownloadResult()
}

fun DownloadResult.onSuccess(action: () -> Unit) {
    if (this is DownloadResult.Success) action()
}

fun DownloadResult.onError(action: (message: String, error: Exception?) -> Unit) {
    if(this is DownloadResult.Error) action(message, error)
}