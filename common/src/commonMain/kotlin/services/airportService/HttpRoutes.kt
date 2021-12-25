package services.airportService

object HttpRoutes {
    private const val BASE_URL = "http://ec2-13-51-70-132.eu-north-1.compute.amazonaws.com/api/v1"
    const val AIRPORTS = "$BASE_URL/airports/"
    const val RUNWAYS = "$BASE_URL/runways/"
    const val FREQUENCIES = "$BASE_URL/frequencies/"
}