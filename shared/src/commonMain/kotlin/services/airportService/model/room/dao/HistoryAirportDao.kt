package services.airportService.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import services.airportService.model.room.HistoryAirportEntity

@Dao
interface HistoryAirportDao {
    
    @Query("SELECT * FROM history_airports ORDER BY timestamp DESC")
    suspend fun getAllHistory(): List<HistoryAirportEntity>
    
    @Query("DELETE FROM history_airports WHERE icao = :icao")
    suspend fun deleteByIcao(icao: String)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyAirport: HistoryAirportEntity)
    
    @Query("DELETE FROM history_airports WHERE timestamp = (SELECT MIN(timestamp) FROM history_airports)")
    suspend fun deleteOldest()
    
    @Query("SELECT COUNT(*) FROM history_airports")
    suspend fun getHistoryCount(): Int
}