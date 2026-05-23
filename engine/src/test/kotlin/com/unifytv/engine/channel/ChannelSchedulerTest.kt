package com.unifytv.engine.channel

import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChannelSchedulerTest {

    private val scheduler = ChannelScheduler()

    private fun item(id: String, durationMs: Long) = MediaItem(
        id = id,
        serverId = "s",
        libraryId = "lib",
        title = id,
        kind = MediaKind.EPISODE,
        durationMs = durationMs,
        playbackUrl = "http://x/$id",
    )

    private val items = listOf(
        item("a", 10_000),
        item("b", 20_000),
        item("c", 30_000),
    ) // cycle = 60_000

    @Test fun `cycle duration is sum of items`() {
        assertEquals(60_000, scheduler.cycleDurationMs(items))
    }

    @Test fun `now playing at anchor is first item at offset zero`() {
        val np = scheduler.nowPlaying(items, anchorEpochMs = 1_000, loop = true, nowMs = 1_000)!!
        assertEquals("a", np.program.item.id)
        assertEquals(0, np.seekOffsetMs)
        assertEquals("b", np.next?.item?.id)
    }

    @Test fun `now playing mid second item computes seek offset`() {
        // 10s (all of a) + 5s into b => offset 5s into b
        val np = scheduler.nowPlaying(items, anchorEpochMs = 0, loop = true, nowMs = 15_000)!!
        assertEquals("b", np.program.item.id)
        assertEquals(5_000, np.seekOffsetMs)
        assertEquals(10_000, np.program.startMs)
        assertEquals(30_000, np.program.endMs)
    }

    @Test fun `looping wraps around after one full cycle`() {
        // 60s = exactly one cycle, so back to a; +2s into the next cycle
        val np = scheduler.nowPlaying(items, anchorEpochMs = 0, loop = true, nowMs = 62_000)!!
        assertEquals("a", np.program.item.id)
        assertEquals(2_000, np.seekOffsetMs)
        assertEquals(60_000, np.program.startMs)
    }

    @Test fun `non-looping channel goes off air after its cycle`() {
        assertNull(scheduler.nowPlaying(items, anchorEpochMs = 0, loop = false, nowMs = 60_000))
    }

    @Test fun `channel that has not started yet is off air`() {
        assertNull(scheduler.nowPlaying(items, anchorEpochMs = 10_000, loop = true, nowMs = 5_000))
    }

    @Test fun `empty channel is off air`() {
        assertNull(scheduler.nowPlaying(emptyList(), anchorEpochMs = 0, loop = true, nowMs = 0))
    }

    @Test fun `programs cover the window without gaps or overlaps`() {
        val window = scheduler.programs(items, anchorEpochMs = 0, loop = true, fromMs = 0, toMs = 70_000)
        // 0-60k is a,b,c then 60k-70k is a (start of next cycle).
        assertEquals(listOf("a", "b", "c", "a"), window.map { it.item.id })
        for (i in 1 until window.size) {
            assertEquals(window[i - 1].endMs, window[i].startMs, "no gap between consecutive programs")
        }
        assertTrue(window.first().startMs <= 0)
        assertTrue(window.last().startMs < 70_000)
    }

    @Test fun `programs window starting mid program includes the in-progress program`() {
        val window = scheduler.programs(items, anchorEpochMs = 0, loop = true, fromMs = 15_000, toMs = 35_000)
        // 15k is mid-b; window should include b (overlaps) and c.
        assertEquals(listOf("b", "c"), window.map { it.item.id })
    }

    @Test fun `shuffle is deterministic for a given seed`() {
        val a = scheduler.order(items, ChannelOrdering.SHUFFLE, seed = 42)
        val b = scheduler.order(items, ChannelOrdering.SHUFFLE, seed = 42)
        assertEquals(a.map { it.id }, b.map { it.id })
    }

    @Test fun `chronological ordering sorts episodes by season then episode`() {
        val e = listOf(
            item("s1e2", 1).copy(seriesName = "Show", seasonNumber = 1, episodeNumber = 2),
            item("s1e1", 1).copy(seriesName = "Show", seasonNumber = 1, episodeNumber = 1),
            item("s2e1", 1).copy(seriesName = "Show", seasonNumber = 2, episodeNumber = 1),
        )
        val ordered = scheduler.order(e, ChannelOrdering.CHRONOLOGICAL, seed = 0)
        assertEquals(listOf("s1e1", "s1e2", "s2e1"), ordered.map { it.id })
    }
}
