package feature.metarParser

enum class CloudsType(val code: String) {
    CLEAR("SKC"), NIL_SIGNIFICANT("NSC"), FEW("FEW"), SCATTERED("SCT"), BROKEN("BKN"), OVERCAST("OVC")
}

enum class CumulusType { CUMULONIMBUS, TOWERING_CUMULUS }

data class CloudLayer(
    val type: CloudsType,
    val lowMarginFt: Int,
    val cumulusType: CumulusType? = null
)