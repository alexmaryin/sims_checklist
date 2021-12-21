package feature.metarscreen.model

data class RunwayWind(
    val lowRunway: Wind,
    val highRunway: Wind
)

sealed class Wind(val cross: Int, val straight: Int) {
    data class LeftCrossHeadWind(val crossKt: Int, val headKt: Int) : Wind(crossKt, headKt)
    data class RightCrossHeadWind(val crossKt: Int, val headKt: Int) : Wind(crossKt, headKt)
    data class LeftCrossTailWind(val crossKt: Int, val tailKt: Int) : Wind(crossKt, tailKt)
    data class RightCrossTailWind(val crossKt: Int, val tailKt: Int) : Wind(crossKt, tailKt)
}

