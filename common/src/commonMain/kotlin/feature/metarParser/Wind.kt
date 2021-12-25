package feature.metarParser

const val METER_TO_KNOT = 1.94384f
const val KPH_TO_KNOT = 0.539957f

enum class WindUnit { KT, MPS, KPH }

data class Wind(
    val direction: Int = 0,
    val variable: Boolean = false,
    val speed: Int = 0,
    val speedUnits: WindUnit = WindUnit.KT,
    val gusts: Int = 0
) {
    val isCalm get() = speed == 0 && direction == 0

    val speedKt: Int get() = when(speedUnits) {
        WindUnit.KT -> speed
        WindUnit.MPS -> (speed * METER_TO_KNOT).toInt()
        WindUnit.KPH -> (speed * KPH_TO_KNOT).toInt()
    }

    val gustsKt: Int get() = when(speedUnits) {
        WindUnit.KT -> gusts
        WindUnit.MPS -> (gusts * METER_TO_KNOT).toInt()
        WindUnit.KPH -> (gusts * KPH_TO_KNOT).toInt()
    }

    val speedMph: Int get() = when(speedUnits) {
        WindUnit.KT -> (speed / METER_TO_KNOT).toInt()
        WindUnit.MPS -> speed
        WindUnit.KPH -> (speed / KPH_TO_KNOT).toInt()
    }

    val gustsMph: Int get() = when(speedUnits) {
        WindUnit.KT -> (gusts / METER_TO_KNOT).toInt()
        WindUnit.MPS -> gusts
        WindUnit.KPH -> (gusts / KPH_TO_KNOT).toInt()
    }
}