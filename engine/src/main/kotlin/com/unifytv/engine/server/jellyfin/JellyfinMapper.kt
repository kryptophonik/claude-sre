package com.unifytv.engine.server.jellyfin

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaKind
import com.unifytv.engine.model.MediaLibrary
import com.unifytv.engine.model.ServerConfig

/** Pure DTO -> domain mapping for Jellyfin/Emby. Kept separate so it is unit-testable. */
internal object JellyfinMapper {

    private const val TICKS_PER_MS = 10_000L

    fun collectionKind(collectionType: String?): MediaKind = when (collectionType) {
        "movies" -> MediaKind.MOVIE
        "tvshows" -> MediaKind.EPISODE
        "musicvideos" -> MediaKind.MUSIC_VIDEO
        "homevideos" -> MediaKind.VIDEO
        else -> MediaKind.OTHER
    }

    fun itemKind(type: String): MediaKind = when (type) {
        "Movie" -> MediaKind.MOVIE
        "Episode" -> MediaKind.EPISODE
        "MusicVideo" -> MediaKind.MUSIC_VIDEO
        "Video" -> MediaKind.VIDEO
        else -> MediaKind.OTHER
    }

    fun toLibrary(config: ServerConfig, dto: JellyfinItem): MediaLibrary = MediaLibrary(
        id = dto.id,
        serverId = config.id,
        name = dto.name,
        kind = collectionKind(dto.collectionType),
    )

    /** Returns null for non-playable rows (folders, series shells, missing duration). */
    fun toMediaItem(config: ServerConfig, libraryId: String, dto: JellyfinItem): MediaItem? {
        val durationMs = (dto.runTimeTicks ?: return null) / TICKS_PER_MS
        if (durationMs <= 0L) return null
        val kind = itemKind(dto.type)
        if (kind == MediaKind.OTHER) return null
        return MediaItem(
            id = dto.id,
            serverId = config.id,
            libraryId = libraryId,
            title = dto.name,
            kind = kind,
            durationMs = durationMs,
            year = dto.productionYear,
            seriesName = dto.seriesName,
            seasonNumber = dto.parentIndexNumber,
            episodeNumber = dto.indexNumber,
            genres = dto.genres,
            posterUrl = imageUrl(config, dto.id, "Primary"),
            backdropUrl = imageUrl(config, dto.id, "Backdrop"),
            playbackUrl = streamUrl(config, dto.id),
        )
    }

    fun streamUrl(config: ServerConfig, itemId: String): String =
        "${config.baseUrl.trimEnd('/')}/Videos/$itemId/stream?static=true&api_key=${config.accessToken}"

    fun imageUrl(config: ServerConfig, itemId: String, type: String): String =
        "${config.baseUrl.trimEnd('/')}/Items/$itemId/Images/$type?api_key=${config.accessToken}"
}
