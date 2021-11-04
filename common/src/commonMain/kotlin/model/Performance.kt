package model

import kotlinx.serialization.Serializable

@Serializable
data class Performance(
    val fuelCapacity: Float,
    val averageCruiseSpeed: Float,
    val averageFuelFlow: Float,
    val taxiFuel: Float = 1f,
    val contingency: Int = 5,
    val reservesMinutes: Int = 45,
)