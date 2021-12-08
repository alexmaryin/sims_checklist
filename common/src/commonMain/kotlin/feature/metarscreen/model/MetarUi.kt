package feature.metarscreen.model

data class MetarUi(
    val airport: String = "",
    val userAngle: Int = 360,
    val metarAngle: Int? = null,
    val metarSpeedKt: Int? = null,
    val rawMetar: String = "",
    val rawTaf: String = ""
)