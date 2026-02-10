package fr.leboncoin.data.mapper

import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.data.database.AlbumEntity
import fr.leboncoin.data.response.AlbumDto

fun AlbumDto.toAlbumEntity() = AlbumEntity(
    albumId = this.albumId ?: 0,
    id = this.id ?: 0,
    title = this.title ?: "",
    url = this.url ?: "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    albumTag = "Album #${this.albumId}",
    trackTag = "Track #${this.id}",
)

fun AlbumEntity.toAlbum() = Album(
    title = this.title,
    thumbnailUrl = this.thumbnailUrl,
    albumTag = this.albumTag,
    trackTag = this.trackTag,
    url = this.url,
)