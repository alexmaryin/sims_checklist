package services.airportService.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import services.airportService.model.room.AirportEntity
import services.airportService.model.room.FrequencyEntity
import services.airportService.model.room.RunwayEntity

@Dao
interface AirportDao {
    
    @Query("SELECT * FROM airports WHERE icao = :icao")
    suspend fun getAirportByIcao(icao: String): AirportEntity?
    
    @Query("SELECT * FROM frequencies WHERE airportIcao = :icao")
    suspend fun getFrequenciesByIcao(icao: String): List<FrequencyEntity>
    
    @Query("SELECT * FROM runways WHERE airportIcao = :icao")
    suspend fun getRunwaysByIcao(icao: String): List<RunwayEntity>
    
    @Transaction
    suspend fun getAirportWithDetails(icao: String): Triple<AirportEntity?, List<FrequencyEntity>, List<RunwayEntity>> {
        val airport = getAirportByIcao(icao)
        val frequencies = getFrequenciesByIcao(icao)
        val runways = getRunwaysByIcao(icao)
        return Triple(airport, frequencies, runways)
    }
    
    @Upsert
    suspend fun insertOrUpdateAirport(airport: AirportEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFrequencies(frequencies: List<FrequencyEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunways(runways: List<RunwayEntity>)
    
    @Query("DELETE FROM frequencies WHERE airportIcao = :icao")
    suspend fun deleteFrequenciesByIcao(icao: String)
    
    @Query("DELETE FROM runways WHERE airportIcao = :icao")
    suspend fun deleteRunwaysByIcao(icao: String)
    
    @Query("SELECT COUNT(*) FROM airports")
    suspend fun getAirportCount(): Long
}