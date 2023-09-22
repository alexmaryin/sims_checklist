package services.metarService.model

import kotlinx.serialization.Serializable

@Serializable
data class WxTaf(
    val data: List<String>,
    val results: Int
)
