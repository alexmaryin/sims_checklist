package feature.metarscreen.model

import kotlinx.serialization.Serializable

@Serializable
data class RunwayWindUi(
    val lowRunway: WindUi,
    val highRunway: WindUi
)

@Serializable
sealed class WindUi(val cross: Int, val straight: Int) {
    data class LeftCrossHeadWindUi(val crossKt: Int, val headKt: Int) : WindUi(crossKt, headKt)
    data class RightCrossHeadWindUi(val crossKt: Int, val headKt: Int) : WindUi(crossKt, headKt)
    data class LeftCrossTailWindUi(val crossKt: Int, val tailKt: Int) : WindUi(crossKt, tailKt)
    data class RightCrossTailWindUi(val crossKt: Int, val tailKt: Int) : WindUi(crossKt, tailKt)
}

