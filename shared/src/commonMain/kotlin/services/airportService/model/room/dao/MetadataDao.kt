package services.airportService.model.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import services.airportService.model.room.MetadataEntity

@Dao
interface MetadataDao {
    @Query("DELETE FROM metadata")
    suspend fun deleteAll()
    
    @Query("SELECT * FROM metadata LIMIT 1")
    suspend fun getMetadata(): MetadataEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMetadata(metadata: MetadataEntity)
}