package services.airportService.model.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class AirportRealm : RealmObject {
    @PrimaryKey val _id: ObjectId = ObjectId.create()
    @Index val icao: String = ""
    val type: String = ""
    val name: String = ""
    val latitude: Float = 0.0f
    val longitude: Float = 0.0f
    val elevation: Int = 0
    val webSite: String? = null
    val wiki: String? = null
    val frequencies: RealmList<FrequencyRealm> = realmListOf()
    val runways: RealmList<RunwayRealm> = realmListOf()
}