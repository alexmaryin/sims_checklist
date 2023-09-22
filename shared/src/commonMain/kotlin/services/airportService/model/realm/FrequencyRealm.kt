package services.airportService.model.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class FrequencyRealm : RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var type: String = ""
    var description: String? = null
    var valueMhz: Float = 0.0F
}