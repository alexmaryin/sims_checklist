package feature.metarscreen.model

data class ErrorUi(
    val type: ErrorType,
    val message: String = ""
)

enum class ErrorType {
    SERVER_ERROR, METAR_PARSE_ERROR
}
