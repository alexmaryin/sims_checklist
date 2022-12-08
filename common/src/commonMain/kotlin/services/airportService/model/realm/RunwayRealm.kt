package services.airportService.model.realm

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RunwayRealm : RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId.create()
    var lengthFeet: Int? = null
    var widthFeet: Int? = null
    var surface: String = ""
    var closed: Boolean = false
    var lowNumber: String = ""
    var lowElevationFeet: Int? = null
    var lowHeading: Int = 0
    var highNumber: String = ""
    var highElevationFeet: Int? = null
    var highHeading: Int = 0
}