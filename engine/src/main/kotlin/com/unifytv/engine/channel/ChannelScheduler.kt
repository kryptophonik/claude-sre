package com.unifytv.engine.channel

import com.unifytv.engine.model.MediaItem
import java.util.Random

/**
 * Turns a [ChannelDefinition] plus a media catalog into a broadcast timeline.
 *
 * The model is intentionally simple and deterministic: a channel's ordered
 * playlist starts at [ChannelDefinition.anchorEpochMs] and, when looping, repeats
 * forever. Given any wall-clock instant the scheduler can answer "what is on
 * now (and how far in)" and "what does the guide look like between A and B".
 * Determinism matters: every client computes the same schedule from the same
 * definition without any shared server state.
 */
class ChannelScheduler {

    /** Resolves [ChannelDefinition.itemIds] against [catalog] and applies ordering. */
    fun resolve(def: ChannelDefinition, catalog: Map<String, MediaItem>): List<MediaItem> {
        val items = def.itemIds.mapNotNull(catalog::get).filter { it.durationMs > 0 }
        return order(items, def.ordering, def.shuffleSeed)
    }

    fun order(items: List<MediaItem>, ordering: ChannelOrdering, seed: Long): List<MediaItem> =
        when (ordering) {
            ChannelOrdering.AS_ADDED -> items
            ChannelOrdering.CHRONOLOGICAL -> items.sortedWith(chronological)
            ChannelOrdering.SHUFFLE -> items.shuffledWith(seed)
        }

    /** Total length of one full pass through the playlist. */
    fun cycleDurationMs(items: List<MediaItem>): Long = items.sumOf { it.durationMs }

    /**
     * The program airing at [nowMs] and the seek offset to make playback feel
     * live, or null when the channel is off air (not started yet, or finished
     * and not looping).
     */
    fun nowPlaying(
        items: List<MediaItem>,
        anchorEpochMs: Long,
        loop: Boolean,
        nowMs: Long,
    ): NowPlaying? {
        val cycle = cycleDurationMs(items)
        if (items.isEmpty() || cycle <= 0L) return null
        val globalIndex = globalIndexAt(items, anchorEpochMs, loop, cycle, nowMs) ?: return null
        val program = programAt(items, anchorEpochMs, loop, cycle, globalIndex) ?: return null
        val next = programAt(items, anchorEpochMs, loop, cycle, globalIndex + 1)
        return NowPlaying(program, seekOffsetMs = nowMs - program.startMs, next = next)
    }

    /**
     * All programs overlapping the window [[fromMs], [toMs]) in air order.
     * Used to render the guide grid.
     */
    fun programs(
        items: List<MediaItem>,
        anchorEpochMs: Long,
        loop: Boolean,
        fromMs: Long,
        toMs: Long,
    ): List<Program> {
        val cycle = cycleDurationMs(items)
        if (items.isEmpty() || cycle <= 0L || toMs <= fromMs) return emptyList()
        val startIndex = globalIndexAt(items, anchorEpochMs, loop, cycle, maxOf(fromMs, anchorEpochMs))
            ?: return emptyList()
        val result = ArrayList<Program>()
        var index = startIndex
        while (true) {
            val program = programAt(items, anchorEpochMs, loop, cycle, index) ?: break
            if (program.startMs >= toMs) break
            if (program.endMs > fromMs) result.add(program)
            index++
        }
        return result
    }

    // --- convenience overloads taking a definition + catalog ---

    fun nowPlaying(def: ChannelDefinition, catalog: Map<String, MediaItem>, nowMs: Long): NowPlaying? =
        nowPlaying(resolve(def, catalog), def.anchorEpochMs, def.loop, nowMs)

    fun programs(def: ChannelDefinition, catalog: Map<String, MediaItem>, fromMs: Long, toMs: Long): List<Program> =
        programs(resolve(def, catalog), def.anchorEpochMs, def.loop, fromMs, toMs)

    // --- internals ---

    /** The program at a global playlist index (index >= itemCount means a later loop). */
    private fun programAt(
        items: List<MediaItem>,
        anchorEpochMs: Long,
        loop: Boolean,
        cycle: Long,
        globalIndex: Int,
    ): Program? {
        val n = items.size
        if (globalIndex < 0) return null
        if (!loop && globalIndex >= n) return null
        val cycleNo = globalIndex / n
        val localIndex = globalIndex % n
        var prefix = 0L
        for (i in 0 until localIndex) prefix += items[i].durationMs
        val start = anchorEpochMs + cycleNo * cycle + prefix
        val item = items[localIndex]
        return Program(item, start, start + item.durationMs)
    }

    /** The global index of the program covering [epochMs], or null when off air. */
    private fun globalIndexAt(
        items: List<MediaItem>,
        anchorEpochMs: Long,
        loop: Boolean,
        cycle: Long,
        epochMs: Long,
    ): Int? {
        val elapsed = epochMs - anchorEpochMs
        if (elapsed < 0L) return null
        if (!loop && elapsed >= cycle) return null
        val cycleNo = if (loop) Math.floorDiv(elapsed, cycle) else 0L
        val pos = elapsed - cycleNo * cycle
        var acc = 0L
        for (i in items.indices) {
            val d = items[i].durationMs
            if (pos < acc + d) return (cycleNo * items.size + i).toInt()
            acc += d
        }
        return null
    }

    private companion object {
        val chronological = compareBy<MediaItem>(
            { it.seriesName ?: it.title },
            { it.seasonNumber ?: Int.MAX_VALUE },
            { it.episodeNumber ?: Int.MAX_VALUE },
            { it.year ?: Int.MAX_VALUE },
            { it.title },
        )

        fun List<MediaItem>.shuffledWith(seed: Long): List<MediaItem> =
            toMutableList().also { it.shuffle(Random(seed)) }
    }
}
