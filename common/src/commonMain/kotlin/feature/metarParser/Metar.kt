package feature.metarParser

import kotlinx.datetime.LocalDateTime

data class Metar(
    val station: String?,
    val reportTime: LocalDateTime?,
    val wind: Wind?,
    val visibility: Visibility?,
    val phenomenons: Phenomenons?,
    val clouds: List<CloudLayer>,
    val temperature: Temperature?,
    val pressureQNH: PressureQNH?,
    val raw: String
) {
    val ceilingAndVisibilityOK get() = clouds.isEmpty() && visibility?.distAll == 9999 && phenomenons != null
}
