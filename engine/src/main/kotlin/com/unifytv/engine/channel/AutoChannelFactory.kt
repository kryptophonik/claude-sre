package com.unifytv.engine.channel

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaKind

/**
 * Generates starter channels automatically from an imported catalog so a user
 * has something to watch immediately after connecting a server. Channels are
 * grouped by genre (and a catch-all per kind), then by series for TV.
 */
class AutoChannelFactory(private val minItemsPerChannel: Int = 3) {

    /**
     * @param anchorEpochMs a shared origin so all generated channels line up on
     *   the same broadcast clock (use the import time).
     */
    fun generate(items: List<MediaItem>, anchorEpochMs: Long): List<ChannelDefinition> {
        if (items.isEmpty()) return emptyList()
        val channels = ArrayList<ChannelDefinition>()
        var number = 1

        // One channel per TV series (binge channels), chronological.
        items.filter { it.kind == MediaKind.EPISODE && it.seriesName != null }
            .groupBy { it.seriesName!! }
            .filterValues { it.size >= minItemsPerChannel }
            .toSortedMap()
            .forEach { (series, eps) ->
                channels += ChannelDefinition(
                    id = "series:$series",
                    number = number++,
                    name = series,
                    ordering = ChannelOrdering.CHRONOLOGICAL,
                    anchorEpochMs = anchorEpochMs,
                    itemIds = eps.map { it.id },
                )
            }

        // One channel per genre across everything else, shuffled.
        val byGenre = LinkedHashMap<String, MutableList<MediaItem>>()
        items.forEach { item ->
            val genres = item.genres.ifEmpty { listOf(item.kind.displayName) }
            genres.forEach { g -> byGenre.getOrPut(g) { mutableListOf() }.add(item) }
        }
        byGenre.toSortedMap()
            .filterValues { it.size >= minItemsPerChannel }
            .forEach { (genre, list) ->
                channels += ChannelDefinition(
                    id = "genre:$genre",
                    number = number++,
                    name = genre,
                    ordering = ChannelOrdering.SHUFFLE,
                    shuffleSeed = genre.hashCode().toLong(),
                    anchorEpochMs = anchorEpochMs,
                    itemIds = list.map { it.id },
                )
            }

        return channels
    }
}

private val MediaKind.displayName: String
    get() = when (this) {
        MediaKind.MOVIE -> "Movies"
        MediaKind.EPISODE -> "TV"
        MediaKind.MUSIC_VIDEO -> "Music"
        MediaKind.VIDEO -> "Videos"
        MediaKind.OTHER -> "Other"
    }
