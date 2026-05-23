package com.unifytv.engine.server.plex

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaKind
import com.unifytv.engine.model.MediaLibrary
import com.unifytv.engine.model.ServerConfig

/** Pure DTO -> domain mapping for Plex. */
internal object PlexMapper {

    fun directoryKind(type: String?): MediaKind = when (type) {
        "movie" -> MediaKind.MOVIE
        "show" -> MediaKind.EPISODE
        else -> MediaKind.OTHER
    }

    fun itemKind(type: String): MediaKind = when (type) {
        "movie" -> MediaKind.MOVIE
        "episode" -> MediaKind.EPISODE
        "clip" -> MediaKind.VIDEO
        else -> MediaKind.OTHER
    }

    fun toLibrary(config: ServerConfig, dto: PlexDirectory): MediaLibrary = MediaLibrary(
        id = dto.key,
        serverId = config.id,
        name = dto.title,
        kind = directoryKind(dto.type),
    )

    fun toMediaItem(config: ServerConfig, libraryId: String, dto: PlexMetadata): MediaItem? {
        val durationMs = dto.duration ?: return null
        if (durationMs <= 0L) return null
        val kind = itemKind(dto.type)
        if (kind == MediaKind.OTHER) return null
        val partKey = dto.media.firstOrNull()?.parts?.firstOrNull()?.key ?: return null
        return MediaItem(
            id = dto.ratingKey,
            serverId = config.id,
            libraryId = libraryId,
            title = dto.title,
            kind = kind,
            durationMs = durationMs,
            year = dto.year,
            seriesName = dto.grandparentTitle,
            seasonNumber = dto.parentIndex,
            episodeNumber = dto.index,
            genres = dto.genres.map { it.tag },
            posterUrl = dto.thumb?.let { resourceUrl(config, it) },
            backdropUrl = dto.art?.let { resourceUrl(config, it) },
            playbackUrl = resourceUrl(config, partKey),
        )
    }

    fun resourceUrl(config: ServerConfig, path: String): String {
        val base = config.baseUrl.trimEnd('/')
        val sep = if ('?' in path) '&' else '?'
        return "$base$path${sep}X-Plex-Token=${config.accessToken}"
    }
}
