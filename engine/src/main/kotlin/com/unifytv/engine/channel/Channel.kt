package com.unifytv.engine.channel

import com.unifytv.engine.model.MediaItem

/** How a channel's playlist is ordered before it is laid out on the timeline. */
enum class ChannelOrdering { SHUFFLE, CHRONOLOGICAL, AS_ADDED }

/**
 * A user-defined virtual channel.
 *
 * The channel behaves like a real broadcast station: its playlist starts at
 * [anchorEpochMs] and plays continuously. When [loop] is true the playlist
 * repeats forever, so there is always something "on air".
 *
 * [itemIds] reference [MediaItem.id]s; the catalog that resolves them is passed
 * to the scheduler so a channel definition stays small and serialisable.
 */
data class ChannelDefinition(
    val id: String,
    val number: Int,
    val name: String,
    val ordering: ChannelOrdering = ChannelOrdering.SHUFFLE,
    val loop: Boolean = true,
    val shuffleSeed: Long = 0L,
    val anchorEpochMs: Long,
    val itemIds: List<String>,
    val logoUrl: String? = null,
    val accentColor: Long? = null,
)

/** A single scheduled airing of an item on a channel timeline. */
data class Program(
    val item: MediaItem,
    val startMs: Long,
    val endMs: Long,
) {
    val durationMs: Long get() = endMs - startMs
    fun contains(epochMs: Long): Boolean = epochMs in startMs until endMs
}

/**
 * The item a channel is showing at a given instant, plus how far into that item
 * playback should be seeked so it feels live.
 */
data class NowPlaying(
    val program: Program,
    val seekOffsetMs: Long,
    val next: Program?,
)
