package com.unifytv.engine.model

/** The kind of media server a [ServerConfig] points at. */
enum class MediaServerKind { JELLYFIN, EMBY, PLEX }

/** Coarse classification of a media item, used for channel building and UI. */
enum class MediaKind { MOVIE, EPISODE, MUSIC_VIDEO, VIDEO, OTHER }

/**
 * Connection details for a single media server.
 *
 * [accessToken] is the API key (Jellyfin/Emby) or the X-Plex-Token (Plex).
 * [userId] is required by Jellyfin/Emby item endpoints; Plex does not use it.
 */
data class ServerConfig(
    val id: String,
    val kind: MediaServerKind,
    val name: String,
    val baseUrl: String,
    val accessToken: String,
    val userId: String? = null,
)

/** A browsable collection on a server (e.g. "Movies", "TV Shows"). */
data class MediaLibrary(
    val id: String,
    val serverId: String,
    val name: String,
    val kind: MediaKind,
)

/**
 * A single playable item, normalised across all server kinds.
 *
 * [playbackUrl] is a directly streamable URL (already authenticated where the
 * server requires a token in the query string).
 */
data class MediaItem(
    val id: String,
    val serverId: String,
    val libraryId: String,
    val title: String,
    val kind: MediaKind,
    val durationMs: Long,
    val year: Int? = null,
    val seriesName: String? = null,
    val seasonNumber: Int? = null,
    val episodeNumber: Int? = null,
    val genres: List<String> = emptyList(),
    val posterUrl: String? = null,
    val backdropUrl: String? = null,
    val playbackUrl: String,
)
