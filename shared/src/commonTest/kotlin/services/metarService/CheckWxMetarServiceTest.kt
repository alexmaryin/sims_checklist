package services.metarService

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.runBlocking
import services.commonApi.forError
import services.commonApi.forSuccess
import kotlin.test.Test
import kotlin.test.assertTrue

internal class CheckWxMetarServiceTest {

    private val httpClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
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