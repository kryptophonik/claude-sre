package com.unifytv.engine.channel

import com.unifytv.engine.model.MediaItem

/** One channel's row in the guide: its definition and the programs in the window. */
data class GuideRow(
    val channel: ChannelDefinition,
    val nowPlaying: NowPlaying?,
    val programs: List<Program>,
)

/** A full guide snapshot across all channels for a time window. */
data class Guide(
    val windowStartMs: Long,
    val windowEndMs: Long,
    val rows: List<GuideRow>,
)

/** Builds [Guide] snapshots from channel definitions and a shared media catalog. */
class GuideBuilder(private val scheduler: ChannelScheduler = ChannelScheduler()) {

    fun build(
        channels: List<ChannelDefinition>,
        catalog: Map<String, MediaItem>,
        nowMs: Long,
        windowMs: Long = DEFAULT_WINDOW_MS,
    ): Guide {
        val end = nowMs + windowMs
        val rows = channels
            .sortedBy { it.number }
            .map { def ->
                val items = scheduler.resolve(def, catalog)
                GuideRow(
                    channel = def,
                    nowPlaying = scheduler.nowPlaying(items, def.anchorEpochMs, def.loop, nowMs),
                    programs = scheduler.programs(items, def.anchorEpochMs, def.loop, nowMs, end),
                )
            }
        return Guide(nowMs, end, rows)
    }

    companion object {
        const val DEFAULT_WINDOW_MS: Long = 3 * 60 * 60 * 1000L // 3 hours
    }
}
