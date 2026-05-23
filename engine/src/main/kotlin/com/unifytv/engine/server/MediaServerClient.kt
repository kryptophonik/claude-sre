package com.unifytv.engine.server

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaLibrary
import com.unifytv.engine.model.ServerConfig

/**
 * A normalised view over a media server. Implementations translate each
 * vendor's API into the shared [MediaItem]/[MediaLibrary] model so the rest of
 * the app never has to care whether a channel was built from Jellyfin, Emby or
 * Plex content.
 */
interface MediaServerClient {
    val config: ServerConfig

    /** Verifies credentials and connectivity; returns the server's display name. */
    suspend fun ping(): String

    /** Lists the user's browsable libraries. */
    suspend fun libraries(): List<MediaLibrary>

    /** Lists all playable items in a library, ready to drop into a channel. */
    suspend fun items(library: MediaLibrary): List<MediaItem>
}
