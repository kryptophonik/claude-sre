package com.unifytv.engine.server.plex

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Plex returns everything wrapped in a MediaContainer; request it as JSON via Accept. */
@Serializable
internal data class PlexResponse(
    @SerialName("MediaContainer") val container: PlexContainer = PlexContainer(),
)

@Serializable
internal data class PlexContainer(
    @SerialName("friendlyName") val friendlyName: String? = null,
    @SerialName("Directory") val directories: List<PlexDirectory> = emptyList(),
    @SerialName("Metadata") val metadata: List<PlexMetadata> = emptyList(),
)

@Serializable
internal data class PlexDirectory(
    @SerialName("key") val key: String,
    @SerialName("title") val title: String = "",
    @SerialName("type") val type: String? = null,
)

@Serializable
internal data class PlexMetadata(
    @SerialName("ratingKey") val ratingKey: String,
    @SerialName("title") val title: String = "",
    @SerialName("type") val type: String = "",
    @SerialName("duration") val duration: Long? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("grandparentTitle") val grandparentTitle: String? = null,
    @SerialName("parentIndex") val parentIndex: Int? = null,
    @SerialName("index") val index: Int? = null,
    @SerialName("thumb") val thumb: String? = null,
    @SerialName("art") val art: String? = null,
    @SerialName("Genre") val genres: List<PlexTag> = emptyList(),
    @SerialName("Media") val media: List<PlexMedia> = emptyList(),
)

@Serializable
internal data class PlexTag(@SerialName("tag") val tag: String = "")

@Serializable
internal data class PlexMedia(@SerialName("Part") val parts: List<PlexPart> = emptyList())

@Serializable
internal data class PlexPart(@SerialName("key") val key: String? = null)
