package com.unifytv.engine.server

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaLibrary
import com.unifytv.engine.model.ServerConfig

/**
 * An in-memory [MediaServerClient] for previews, tests and the demo mode shown
 * before any real server is connected.
 */
class FakeMediaServerClient(
    override val config: ServerConfig,
    private val libraries: List<MediaLibrary>,
    private val itemsByLibrary: Map<String, List<MediaItem>>,
) : MediaServerClient {
    override suspend fun ping(): String = config.name
    override suspend fun libraries(): List<MediaLibrary> = libraries
    override suspend fun items(library: MediaLibrary): List<MediaItem> =
        itemsByLibrary[library.id].orEmpty()
}
