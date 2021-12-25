package feature.metarParser

enum class VisibilityUnit { METERS, SM }

enum class VisibilityDirection { NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST }

data class Visibility(
    val distAll: Int? = null,
    val byDirections: List<VisibilityByDir> = emptyList(),
    val byRunways: List<VisibilityByRunway> = emptyList(),
    val distUnits: VisibilityUnit = VisibilityUnit.METERS,
    )

data class VisibilityByDir(
    val dist: Int,
    val direction: VisibilityDirection
)

data class VisibilityByRunway(
    val dist: Int,
    val runway: String
)