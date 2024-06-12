package services.airportService.model.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class HistoryAirportRealm : RealmObject {
    @PrimaryKey var timestamp: Long = 0L
    var icao: String = ""
    var name: String = ""
}