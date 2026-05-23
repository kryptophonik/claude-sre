package com.unifytv.engine.server.jellyfin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Jellyfin and Emby share this API shape, so the same DTOs cover both. */
@Serializable
internal data class JellyfinSystemInfo(
    @SerialName("ServerName") val serverName: String = "",
)

@Serializable
internal data class JellyfinItemsResponse(
    @SerialName("Items") val items: List<JellyfinItem> = emptyList(),
)

@Serializable
internal data class JellyfinItem(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String = "",
    @SerialName("Type") val type: String = "",
    @SerialName("CollectionType") val collectionType: String? = null,
    @SerialName("RunTimeTicks") val runTimeTicks: Long? = null,
    @SerialName("ProductionYear") val productionYear: Int? = null,
    @SerialName("SeriesName") val seriesName: String? = null,
    @SerialName("ParentIndexNumber") val parentIndexNumber: Int? = null,
    @SerialName("IndexNumber") val indexNumber: Int? = null,
    @SerialName("Genres") val genres: List<String> = emptyList(),
)
