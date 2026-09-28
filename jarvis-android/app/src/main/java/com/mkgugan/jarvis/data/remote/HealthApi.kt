package com.mkgugan.jarvis.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

sealed interface HealthResult {
    data object Online : HealthResult
    data class Unavailable(val reason: String) : HealthResult
}

class HealthApi(
    private val baseUrl: String = DEFAULT_BASE_URL,
    private val client: OkHttpClient = defaultClient(),
) {
    suspend fun checkHealth(): HealthResult = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("$baseUrl/api/v1/health")
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext HealthResult.Unavailable("HTTP ${response.code}")
                }
                val body = response.body?.string().orEmpty()
                if (body.contains("\"status\"") && body.contains("online")) {
                    HealthResult.Online
                } else {
                    HealthResult.Unavailable("Unexpected response body")
                }
            }
        } catch (e: IOException) {
            HealthResult.Unavailable(e.message ?: "Network error")
        }
    }

    companion object {
        const val DEFAULT_BASE_URL = "http://127.0.0.1:8000"

        fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }
}
