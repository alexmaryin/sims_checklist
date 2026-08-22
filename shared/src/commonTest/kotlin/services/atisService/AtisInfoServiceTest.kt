package services.atisService

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import services.commonApi.ErrorType
import services.commonApi.forError
import services.commonApi.forSuccess
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AtisInfoServiceTest {

    private fun createService(responses: Map<String, Pair<String, HttpStatusCode>>): AtisInfoService {
        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            val (body, status) = responses.entries.firstOrNull { url.contains(it.key) }?.value
                ?: ("[]" to HttpStatusCode.NotFound)
            respond(
                content = ByteReadChannel(body),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return AtisInfoService(httpClient)
    }

    @Test
    fun `getDatis for KJFK returns single COMBINED entry`() = runBlocking {
        val service = createService(
            mapOf("KJFK" to ("""[{"airport":"KJFK","type":"combined","code":"A","datis":"INFO A 1852Z","time":"1852","updatedAt":"2026-08-22T18:52:00Z"}]""" to HttpStatusCode.OK))
        )

        val result = service.getDatis("KJFK")
        result.forSuccess { entries ->
            assertEquals(1, entries.size)
            assertEquals("KJFK", entries[0].airport)
            assertEquals(services.atisService.model.AtisType.COMBINED, entries[0].type)
            assertEquals("A", entries[0].code)
        }
        result.forError { _, _ -> throw AssertionError("Expected success") }
    }

    @Test
    fun `getDatis for KATL returns ARRIVAL and DEPARTURE entries`() = runBlocking {
        val service = createService(
            mapOf("KATL" to ("""[{"airport":"KATL","type":"arr","code":"C","datis":"ARRIVAL INFO C","time":"0852","updatedAt":"2026-08-22T08:52:00Z"},{"airport":"KATL","type":"dep","code":"P","datis":"DEPARTURE INFO P","time":"0852","updatedAt":"2026-08-22T08:52:00Z"}]""" to HttpStatusCode.OK))
        )

        val result = service.getDatis("KATL")
        result.forSuccess { entries ->
            assertEquals(2, entries.size)
            assertEquals(services.atisService.model.AtisType.ARRIVAL, entries[0].type)
            assertEquals(services.atisService.model.AtisType.DEPARTURE, entries[1].type)
        }
        result.forError { _, _ -> throw AssertionError("Expected success") }
    }

    @Test
    fun `getDatis for non-US station returns EMPTY_RESULT`() = runBlocking {
        val service = createService(
            mapOf("UWLW" to ("""{"error":"No results found"}""" to HttpStatusCode.OK))
        )

        val result = service.getDatis("UWLW")
        result.forError { type, _ ->
            assertEquals(ErrorType.EMPTY_RESULT, type)
        }
        result.forSuccess { throw AssertionError("Expected EMPTY_RESULT error") }
    }

    @Test
    fun `getDatis handles HTTP error status`() = runBlocking {
        val service = createService(
            mapOf("KJFK" to ("Internal Server Error" to HttpStatusCode.InternalServerError))
        )

        val result = service.getDatis("KJFK")
        result.forError { type, _ ->
            assertEquals(ErrorType.UNKNOWN, type)
        }
        result.forSuccess { throw AssertionError("Expected error") }
    }

    @Test
    fun `getDatis handles malformed JSON`() = runBlocking {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("not json at all"),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        val service = AtisInfoService(httpClient)

        val result = service.getDatis("KJFK")
        result.forError { type, _ ->
            assertEquals(ErrorType.UNKNOWN, type)
        }
        result.forSuccess { throw AssertionError("Expected error") }
    }
}
