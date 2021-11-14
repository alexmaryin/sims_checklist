package feature.metarscreen.model

data class ErrorUi(
    val type: ErrorType,
    val message: String = ""
)

enum class ErrorType {
    CLIENT_ERROR, SERVER_ERROR, METAR_PARSE_ERROR, NO_CONNECTION_ERROR, UNKNOWN_ERROR
}
