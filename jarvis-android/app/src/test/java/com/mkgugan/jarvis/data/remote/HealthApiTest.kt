package com.mkgugan.jarvis.data.remote

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HealthApiTest {

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `returns Online for a 200 response with status online`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{"status":"online"}""")
        )
        val api = HealthApi(baseUrl = server.url("").toString().trimEnd('/'))

        val result = api.checkHealth()

        assertEquals(HealthResult.Online, result)
    }

    @Test
    fun `returns Unavailable for a non-2xx response`() = runTest {
        server.enqueue(MockResponse().setResponseCode(503))
        val api = HealthApi(baseUrl = server.url("").toString().trimEnd('/'))

        val result = api.checkHealth()

        assertTrue(result is HealthResult.Unavailable)
        assertEquals("HTTP 503", (result as HealthResult.Unavailable).reason)
    }

    @Test
    fun `returns Unavailable when the server is unreachable`() = runTest {
        server.shutdown()
        val api = HealthApi(baseUrl = server.url("").toString().trimEnd('/'))

        val result = api.checkHealth()

        assertTrue(result is HealthResult.Unavailable)
    }
}
