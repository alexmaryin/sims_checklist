package services.airportService.model.realm

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RunwayRealm : RealmObject {
    @PrimaryKey val _id: ObjectId = ObjectId.create()
    val lengthFeet: Int? = null
    val widthFeet: Int? = null
    val surface: String = ""
    val closed: Boolean = false
    val lowNumber: String = ""
    val lowElevationFeet: Int? = null
    val lowHeading: Int = 0
    val highNumber: String = ""
    val highElevationFeet: Int? = null
    val highHeading: Int = 0
}