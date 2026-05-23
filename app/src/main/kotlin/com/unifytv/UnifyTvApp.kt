package com.unifytv

import android.app.Application
import com.unifytv.data.MediaRepository
import com.unifytv.engine.server.MediaServerClients
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

/** Minimal manual DI: one HTTP client and one repository for the whole app. */
class UnifyTvApp : Application() {

    val httpClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(ContentNegotiation) { json(MediaServerClients.json) }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
            }
        }
    }

    val repository: MediaRepository by lazy {
        MediaRepository(httpClient).also { it.loadDemo() }
    }
}
