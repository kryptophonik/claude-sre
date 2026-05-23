package com.unifytv.data

import com.unifytv.engine.channel.AutoChannelFactory
import com.unifytv.engine.channel.ChannelDefinition
import com.unifytv.engine.model.MediaItem
import com.unifytv.engine.model.ServerConfig
import com.unifytv.engine.server.MediaServerClients
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Owns the aggregated catalog and the channel line-up. Connecting a server
 * imports its items, merges them into one catalog spanning every server, then
 * regenerates channels so Jellyfin, Emby and Plex content can share a channel.
 */
class MediaRepository(
    private val http: HttpClient,
    private val autoChannels: AutoChannelFactory = AutoChannelFactory(),
) {
    private val _catalog = MutableStateFlow<Map<String, MediaItem>>(emptyMap())
    val catalog: StateFlow<Map<String, MediaItem>> = _catalog.asStateFlow()

    private val _channels = MutableStateFlow<List<ChannelDefinition>>(emptyList())
    val channels: StateFlow<List<ChannelDefinition>> = _channels.asStateFlow()

    private val _servers = MutableStateFlow<List<ServerConfig>>(emptyList())
    val servers: StateFlow<List<ServerConfig>> = _servers.asStateFlow()

    /** A common broadcast origin so every channel lines up on the same clock. */
    private val anchorEpochMs = System.currentTimeMillis()

    fun loadDemo() {
        val items = DemoContent.items
        _catalog.value = items.associateBy { it.id }
        _channels.value = DemoContent.channels(anchorEpochMs)
    }

    /** Connects to [config], imports every library, and rebuilds the line-up. */
    suspend fun connectAndImport(config: ServerConfig): Result<Int> = runCatching {
        val client = MediaServerClients.create(config, http)
        client.ping()
        val imported = client.libraries().flatMap { client.items(it) }

        val merged = _catalog.value.toMutableMap()
        imported.forEach { merged[it.id] = it }
        _catalog.value = merged

        if (_servers.value.none { it.id == config.id }) {
            _servers.value = _servers.value + config
        }
        _channels.value = autoChannels.generate(merged.values.toList(), anchorEpochMs)
        imported.size
    }

    fun channel(id: String): ChannelDefinition? = _channels.value.firstOrNull { it.id == id }
}
