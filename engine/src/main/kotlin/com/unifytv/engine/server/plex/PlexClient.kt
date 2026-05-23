package com.unifytv.engine.server.plex

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaLibrary
import com.unifytv.engine.model.ServerConfig
import com.unifytv.engine.server.MediaServerClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

/** Talks to a Plex Media Server. Plex authenticates with an X-Plex-Token. */
class PlexClient(
    override val config: ServerConfig,
    private val http: HttpClient,
) : MediaServerClient {

    private val base = config.baseUrl.trimEnd('/')

    override suspend fun ping(): String {
        val resp: PlexResponse = http.get(base) { plex() }.body()
        return resp.container.friendlyName?.ifBlank { config.name } ?: config.name
    }

    override suspend fun libraries(): List<MediaLibrary> {
        val resp: PlexResponse = http.get("$base/library/sections") { plex() }.body()
        return resp.container.directories
            .map { PlexMapper.toLibrary(config, it) }
    }

    override suspend fun items(library: MediaLibrary): List<MediaItem> {
        // type=4 == episodes for show libraries; "all" returns top-level for movies.
        val path = if (library.kind.name == "EPISODE") {
            "$base/library/sections/${library.id}/all?type=4"
        } else {
            "$base/library/sections/${library.id}/all"
        }
        val resp: PlexResponse = http.get(path) { plex() }.body()
        return resp.container.metadata.mapNotNull { PlexMapper.toMediaItem(config, library.id, it) }
    }

    private fun io.ktor.client.request.HttpRequestBuilder.plex() {
        header("Accept", "application/json")
        parameter("X-Plex-Token", config.accessToken)
    }
}
