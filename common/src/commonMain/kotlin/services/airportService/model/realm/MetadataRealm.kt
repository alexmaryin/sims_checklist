package services.airportService.model.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class MetadataRealm : RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var updateTimestamp: Long = 0L
    var airportsCount: Long = 0L
}