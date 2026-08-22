package feature.metarscreen

import kotlin.test.Test
import kotlin.test.assertEquals

internal class MetarScannerTest {

    @Test
    fun `Loading state includes loadAtis`() {
        val loading = MetarScanner.Loading(
            loadMetar = false,
            loadTaf = false,
            loadAirport = false,
            loadAtis = true
        )
        assertEquals(true, loading.state)
    }

    @Test
    fun `Loading state is false when all flags are false`() {
        val loading = MetarScanner.Loading(
            loadMetar = false,
            loadTaf = false,
            loadAirport = false,
            loadAtis = false
        )
        assertEquals(false, loading.state)
    }

    @Test
    fun `Loading state is true when any flag is true`() {
        val loading = MetarScanner.Loading(
            loadMetar = true,
            loadTaf = false,
            loadAirport = false,
            loadAtis = false
        )
        assertEquals(true, loading.state)
    }
}
