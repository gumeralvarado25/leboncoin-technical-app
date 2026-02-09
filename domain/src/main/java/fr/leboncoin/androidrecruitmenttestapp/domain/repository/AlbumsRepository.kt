package fr.leboncoin.androidrecruitmenttestapp.domain.repository

import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult

interface AlbumsRepository {
    suspend fun getAlbums(): AlbumResult<List<Album>>
}