package model

data class Performance(
    val fuelCapacity: Float,
    val averageCruiseSpeed: Float,
    val averageFuelFlow: Float,
    val taxiFuel: Float = 1f,
    val contingency: Float = 0.05f,
    val reservesMinutes: Int = 45,
)
