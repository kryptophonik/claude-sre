package com.unifytv.engine.server.jellyfin

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaLibrary
import com.unifytv.engine.model.ServerConfig
import com.unifytv.engine.server.MediaServerClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** Talks to Jellyfin and Emby (identical API surface) over their REST API. */
class JellyfinClient(
    override val config: ServerConfig,
    private val http: HttpClient,
) : MediaServerClient {

    private val base = config.baseUrl.trimEnd('/')
    private val userId: String get() = requireNotNull(config.userId) { "Jellyfin/Emby require a userId" }

    override suspend fun ping(): String {
        val info: JellyfinSystemInfo = http.get("$base/System/Info") {
            parameter("api_key", config.accessToken)
        }.body()
        return info.serverName.ifBlank { config.name }
    }

    override suspend fun libraries(): List<MediaLibrary> {
        val resp: JellyfinItemsResponse = http.get("$base/Users/$userId/Views") {
            parameter("api_key", config.accessToken)
        }.body()
        return resp.items.map { JellyfinMapper.toLibrary(config, it) }
    }

    override suspend fun items(library: MediaLibrary): List<MediaItem> {
        val resp: JellyfinItemsResponse = http.get("$base/Users/$userId/Items") {
            parameter("ParentId", library.id)
            parameter("Recursive", "true")
            parameter("IncludeItemTypes", "Movie,Episode,MusicVideo,Video")
            parameter("Fields", "Genres,ProductionYear,RunTimeTicks,SeriesName")
            parameter("api_key", config.accessToken)
        }.body()
        return resp.items.mapNotNull { JellyfinMapper.toMediaItem(config, library.id, it) }
    }
}
