package services.airportService.localService

import services.airportService.AirportService
import services.airportService.model.Airport
import services.airportService.model.Frequency
import services.airportService.model.HistoryAirport
import services.airportService.model.LastUpdate
import services.airportService.model.Runway
import services.airportService.model.room.AirportDatabase
import services.airportService.model.room.HistoryAirportEntity
import services.airportService.model.room.toDomain
import services.commonApi.ErrorType
import services.commonApi.Result

class AirportServiceRoomImpl(
    private val database: AirportDatabase
) : AirportService {

    override suspend fun isEmpty() = database.airportDao().isEmpty()

    override suspend fun dropAll() {
        val dao = database.airportDao()
        dao.deleteFrequencies()
        dao.deleteRunways()
        dao.deleteAirports()
    }

    override suspend fun getLastUpdate(): LastUpdate? {
        val metadata = database.metadataDao().getMetadata()
        return metadata?.toDomain()
    }

    private suspend fun addAirportToHistory(airport: Airport) {
        val historyDao = database.historyDao()
        
        // Delete similar airports from history first
        historyDao.deleteByIcao(airport.icao)
        
        // Add as the latest one
        val historyEntity = HistoryAirportEntity(
            timestamp = System.currentTimeMillis(),
            icao = airport.icao,
            name = airport.name
        )
        historyDao.insertHistory(historyEntity)
        
        // Check for list overflow and delete the oldest if needed
        val historyCount = historyDao.getHistoryCount()
        if (historyCount > 10) {
            historyDao.deleteOldest()
        }
    }

    override suspend fun getAirportByICAO(icao: String): Result<Airport> {
        val airportDao = database.airportDao()
        val (airport, frequencies, runways) = airportDao.getAirportWithDetails(icao)
        
        return if (airport != null) {
            val domainAirport = airport.toDomain(frequencies, runways)
            addAirportToHistory(domainAirport)
            Result.Success(domainAirport)
        } else {
            Result.Error(ErrorType.BAD_REQUEST, "No airport found with ICAO $icao")
        }
    }

    override suspend fun getRunwaysByICAO(icao: String): Result<List<Runway>> {
        val airportDao = database.airportDao()
        val runways = airportDao.getRunwaysByIcao(icao)
        return if (runways.isNotEmpty()) {
            Result.Success(runways.map { it.toDomain() })
        } else {
            Result.Error(ErrorType.BAD_REQUEST, "No runways found for airport with ICAO $icao")
        }
    }

    override suspend fun getFrequenciesByICAO(icao: String): Result<List<Frequency>> {
        val airportDao = database.airportDao()
        val frequencies = airportDao.getFrequenciesByIcao(icao)
        return if (frequencies.isNotEmpty()) {
            Result.Success(frequencies.map { it.toDomain() })
        } else {
            Result.Error(ErrorType.BAD_REQUEST, "No frequencies found for airport with ICAO $icao")
        }
    }

    override suspend fun getAirportsHistory(): Result<List<HistoryAirport>> {
        val historyDao = database.historyDao()
        val historyList = historyDao.getAllHistory()
        
        return if (historyList.isNotEmpty()) {
            Result.Success(historyList.map { it.toDomain() })
        } else {
            Result.Error(ErrorType.OTHER_CLIENT_ERROR, "Empty history list")
        }
    }

    override suspend fun searchAirports(search: String, limit: Int): Result<List<Airport>> {
        val dao = database.airportDao()
        val searchResult = if (search.isNotBlank()) dao.searchAirportsByIcaoOrName(search, limit)
            else dao.getFirstBatchOfAirports(limit)
        return if (searchResult.isNotEmpty()) {
            Result.Success(searchResult.map { it.toDomain() })
        } else {
            Result.Error(ErrorType.EMPTY_RESULT, "Nothing was found")
        }
    }
}