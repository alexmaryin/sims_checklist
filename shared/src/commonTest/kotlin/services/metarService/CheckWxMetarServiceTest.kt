package services.metarService

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import services.commonApi.forError
import services.commonApi.forSuccess
import kotlin.test.Test
import kotlin.test.assertTrue

internal class CheckWxMetarServiceTest {

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    @Test
    fun `get METAR and TAF info from site should return success`() = runBlocking {
        with (CheckWxMetarService(httpClient)) {
            val result = getMetar("UUWW")
            result.forSuccess {
                println(it)
                assertTrue { it.icao == "UUWW" }
            }
            result.forError { type, message ->
                throw AssertionError("$type: $message")
            }
        }
    }
}