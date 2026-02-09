package fr.leboncoin.data.repository

import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.repository.AlbumsRepository
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumError
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import fr.leboncoin.data.database.AlbumDatabase
import fr.leboncoin.data.mapper.toAlbum
import fr.leboncoin.data.mapper.toAlbumEntity
import fr.leboncoin.data.network.api.AlbumApiService
import java.net.UnknownHostException

class AlbumsRepositoryImpl(
    private val albumApiService: AlbumApiService,
    private val albumDatabase: AlbumDatabase,
): AlbumsRepository {

    override suspend fun getAlbums(): AlbumResult<List<Album>> {
        return try {
            val albums = albumApiService.getAlbums()
            albumDatabase.dao.upsertAlbums(albums.map { it.toAlbumEntity() })
            AlbumResult.Success(getAlbumsFromDatabase())
        } catch (throwable: Throwable) {
            if (throwable is UnknownHostException)
                AlbumResult.Success(getAlbumsFromDatabase())
            else
                AlbumResult.Failure(AlbumError.Unknown)
        }
    }

    private suspend fun getAlbumsFromDatabase(): List<Album> =
        albumDatabase.dao.getAlbums().map { it.toAlbum() }
}