package fr.leboncoin.data.mapper

import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.data.database.AlbumEntity
import fr.leboncoin.data.response.AlbumDto

fun AlbumDto.toAlbumEntity(isFavorite: Boolean) = AlbumEntity(
    albumId = this.albumId ?: 0,
    id = this.id ?: 0,
    title = this.title ?: "",
    url = this.url ?: "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    albumTag = "Album #${this.albumId}",
    trackTag = "Track #${this.id}",
    isFavorite = isFavorite,
)

fun AlbumEntity.toAlbum() = Album(
    id = this.id,
    title = this.title,
    thumbnailUrl = this.thumbnailUrl,
    albumTag = this.albumTag,
    trackTag = this.trackTag,
    url = this.url,
    isFavorite = this.isFavorite,
)