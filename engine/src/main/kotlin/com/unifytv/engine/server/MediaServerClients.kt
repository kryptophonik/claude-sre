package com.unifytv.engine.server

import com.unifytv.engine.model.MediaServerKind
import com.unifytv.engine.model.ServerConfig
import com.unifytv.engine.server.jellyfin.JellyfinClient
import com.unifytv.engine.server.plex.PlexClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/** Builds the right [MediaServerClient] for a server config. */
object MediaServerClients {

    fun create(config: ServerConfig, http: HttpClient): MediaServerClient = when (config.kind) {
        MediaServerKind.JELLYFIN, MediaServerKind.EMBY -> JellyfinClient(config, http)
        MediaServerKind.PLEX -> PlexClient(config, http)
    }

    /** A lenient JSON config; servers add fields between releases, so ignore unknowns. */
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    /** Configures an [HttpClient] (any engine) for talking to media servers. */
    fun configure(http: HttpClient): HttpClient = http.config {
        install(ContentNegotiation) { json(json) }
    }
}
