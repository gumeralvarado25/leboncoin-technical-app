package fr.leboncoin.data.response

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: Int? = null,
    val albumId: Int? = null,
    val title: String? = null,
    val url: String? = null,
    val thumbnailUrl: String? = null,
)