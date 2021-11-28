package feature.remote.airportService

object HttpRoutes {
    private const val BASE_URL = "https://github.com/davidmegginson/ourairports-data"
    const val AIRPORTS = "$BASE_URL/blob/main/airports.csv"
    const val RUNWAYS = "$BASE_URL/blob/main/runways.csv"
    const val COUNTRIES = "$BASE_URL/blob/main/countries.csv"
}