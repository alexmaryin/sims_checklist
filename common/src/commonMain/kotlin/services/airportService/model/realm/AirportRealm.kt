package services.airportService.model.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class AirportRealm : RealmObject {
    @PrimaryKey var icao: String = ""
    var type: String = ""
    var name: String = ""
    var latitude: Float = 0.0f
    var longitude: Float = 0.0f
    var elevation: Int = 0
    var webSite: String? = null
    var wiki: String? = null
    var frequencies: RealmList<FrequencyRealm> = realmListOf()
    var runways: RealmList<RunwayRealm> = realmListOf()
}