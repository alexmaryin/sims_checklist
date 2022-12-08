package services.airportService.model.realm

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class FrequencyRealm : RealmObject {
    @PrimaryKey val _id: ObjectId = ObjectId.create()
    val type: String = ""
    val description: String? = null
    val valueMhz: Float = 0.0F
}