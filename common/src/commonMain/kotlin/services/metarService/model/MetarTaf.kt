package services.metarService.model

import kotlinx.serialization.Serializable

@Serializable
data class MetarTaf(
    val icao: String,
    val name: String,
    val metar: String,
    val taf: String
)