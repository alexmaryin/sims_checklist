package feature.metarscreen.model

typealias Heading = Int

data class MetarUi(
    val airport: String = "",
    val userAngle: Heading = 360,
    val userRunwayHead: Heading = 360,
    val metarAngle: Int? = null,
    val metarSpeedKt: Int? = null,
    val rawMetar: String = "",
    val rawTaf: String = ""
)