package feature.metarscreen.model

typealias Heading = Int

data class MetarUi(
    val airport: String = "",
    val userAngle: Heading = 360,
    val metarAngle: Heading? = null,
    val metarSpeedKt: Int? = null,
    val rawMetar: String = "",
    val rawTaf: String = ""
)