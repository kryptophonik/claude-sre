package com.unifytv.data

import com.unifytv.engine.channel.AutoChannelFactory
import com.unifytv.engine.channel.ChannelDefinition
import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.MediaKind

/**
 * Sample catalog shown before any real server is connected, so the guide is
 * never empty and the UI can be explored immediately.
 */
object DemoContent {

    private fun movie(id: String, title: String, mins: Int, year: Int, vararg genres: String) =
        MediaItem(
            id = id, serverId = "demo", libraryId = "movies", title = title,
            kind = MediaKind.MOVIE, durationMs = mins * 60_000L, year = year,
            genres = genres.toList(), playbackUrl = "",
        )

    private fun episode(id: String, series: String, s: Int, e: Int, title: String, mins: Int, vararg genres: String) =
        MediaItem(
            id = id, serverId = "demo", libraryId = "tv", title = title,
            kind = MediaKind.EPISODE, durationMs = mins * 60_000L, seriesName = series,
            seasonNumber = s, episodeNumber = e, genres = genres.toList(), playbackUrl = "",
        )

    val items: List<MediaItem> = listOf(
        movie("m1", "Neon Skyline", 112, 2021, "Sci-Fi", "Drama"),
        movie("m2", "The Long Quiet", 98, 2019, "Drama"),
        movie("m3", "Afterburn", 131, 2023, "Action", "Sci-Fi"),
        movie("m4", "Paper Moons", 89, 2018, "Comedy"),
        movie("m5", "Glass Harbor", 104, 2022, "Drama", "Mystery"),
        movie("m6", "Run Lola Run It Back", 84, 2020, "Action", "Comedy"),
        episode("e1", "Static City", 1, 1, "Cold Open", 42, "Sci-Fi"),
        episode("e2", "Static City", 1, 2, "Signal Lost", 44, "Sci-Fi"),
        episode("e3", "Static City", 1, 3, "Uplink", 41, "Sci-Fi"),
        episode("e4", "Static City", 1, 4, "Downlink", 43, "Sci-Fi"),
        episode("e5", "Kitchen Confidante", 1, 1, "Mise en Place", 22, "Comedy"),
        episode("e6", "Kitchen Confidante", 1, 2, "Service", 23, "Comedy"),
        episode("e7", "Kitchen Confidante", 1, 3, "86'd", 21, "Comedy"),
    )

    fun channels(anchorEpochMs: Long): List<ChannelDefinition> =
        AutoChannelFactory(minItemsPerChannel = 2).generate(items, anchorEpochMs)
}
