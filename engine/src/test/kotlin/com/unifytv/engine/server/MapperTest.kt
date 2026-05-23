package com.unifytv.engine.server

import com.unifytv.engine.model.MediaKind
import com.unifytv.engine.model.MediaServerKind
import com.unifytv.engine.model.ServerConfig
import com.unifytv.engine.server.jellyfin.JellyfinItemsResponse
import com.unifytv.engine.server.jellyfin.JellyfinMapper
import com.unifytv.engine.server.plex.PlexMapper
import com.unifytv.engine.server.plex.PlexResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MapperTest {

    private val json = MediaServerClients.json

    private val jellyfinConfig = ServerConfig(
        id = "jf", kind = MediaServerKind.JELLYFIN, name = "Home",
        baseUrl = "http://host:8096", accessToken = "KEY", userId = "u1",
    )

    private val plexConfig = ServerConfig(
        id = "px", kind = MediaServerKind.PLEX, name = "Plex",
        baseUrl = "http://host:32400", accessToken = "TOK",
    )

    @Test fun `jellyfin item parses and converts ticks to milliseconds`() {
        val body = """
            {"Items":[
              {"Id":"1","Name":"Pilot","Type":"Episode","RunTimeTicks":12000000000,
               "ProductionYear":2010,"SeriesName":"My Show","ParentIndexNumber":1,
               "IndexNumber":1,"Genres":["Comedy"]},
              {"Id":"2","Name":"A Folder","Type":"Folder"}
            ]}
        """.trimIndent()
        val resp = json.decodeFromString(JellyfinItemsResponse.serializer(), body)
        val items = resp.items.mapNotNull { JellyfinMapper.toMediaItem(jellyfinConfig, "lib", it) }

        assertEquals(1, items.size, "folder without runtime is dropped")
        val ep = items.single()
        assertEquals("Pilot", ep.title)
        assertEquals(MediaKind.EPISODE, ep.kind)
        assertEquals(1_200_000, ep.durationMs) // 12,000,000,000 ticks / 10,000
        assertEquals("My Show", ep.seriesName)
        assertTrue(ep.playbackUrl.startsWith("http://host:8096/Videos/1/stream"))
        assertTrue(ep.playbackUrl.contains("api_key=KEY"))
    }

    @Test fun `plex metadata parses duration genres and token appended stream url`() {
        val body = """
            {"MediaContainer":{"friendlyName":"Plex","Metadata":[
              {"ratingKey":"55","title":"Blade Runner","type":"movie","duration":6900000,
               "year":1982,"thumb":"/library/metadata/55/thumb/1","Genre":[{"tag":"Sci-Fi"}],
               "Media":[{"Part":[{"key":"/library/parts/9/file.mkv"}]}]},
              {"ratingKey":"56","title":"No Media","type":"movie","duration":1000}
            ]}}
        """.trimIndent()
        val resp = json.decodeFromString(PlexResponse.serializer(), body)
        val items = resp.container.metadata.mapNotNull { PlexMapper.toMediaItem(plexConfig, "1", it) }

        assertEquals(1, items.size, "item without a media part is dropped")
        val movie = items.single()
        assertEquals("Blade Runner", movie.title)
        assertEquals(MediaKind.MOVIE, movie.kind)
        assertEquals(6_900_000, movie.durationMs)
        assertEquals(listOf("Sci-Fi"), movie.genres)
        assertEquals("http://host:32400/library/parts/9/file.mkv?X-Plex-Token=TOK", movie.playbackUrl)
    }

    @Test fun `unknown jellyfin fields are ignored`() {
        val body = """{"Items":[{"Id":"1","Name":"x","Type":"Movie","RunTimeTicks":10000,"BrandNewField":42}]}"""
        val resp = json.decodeFromString(JellyfinItemsResponse.serializer(), body)
        assertEquals("x", resp.items.single().name)
    }

    @Test fun `zero duration plex item is dropped`() {
        val body = """{"MediaContainer":{"Metadata":[
            {"ratingKey":"1","title":"z","type":"movie","duration":0,
             "Media":[{"Part":[{"key":"/p"}]}]}]}}"""
        val resp = json.decodeFromString(PlexResponse.serializer(), body)
        assertNull(PlexMapper.toMediaItem(plexConfig, "1", resp.container.metadata.single()))
    }
}
