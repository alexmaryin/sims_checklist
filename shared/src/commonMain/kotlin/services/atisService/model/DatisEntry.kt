package services.atisService.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DatisEntry(
    val airport: String,
    val type: AtisType,
    val code: String,
    val datis: String,
    val time: String,
    val updatedAt: String
)

@Serializable
enum class AtisType {
    @SerialName("combined") COMBINED,
    @SerialName("arr") ARRIVAL,
    @SerialName("dep") DEPARTURE
}
