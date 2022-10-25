package services.metarService.model

import kotlinx.serialization.Serializable

@Serializable
data class WxMetar(
    val data: List<String>,
    val results: Int
)
