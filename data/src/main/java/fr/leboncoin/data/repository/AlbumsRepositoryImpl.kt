package fr.leboncoin.data.repository

import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.repository.AlbumsRepository
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumError
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import fr.leboncoin.data.database.AlbumDatabase
import fr.leboncoin.data.mapper.toAlbum
import fr.leboncoin.data.mapper.toAlbumEntity
import fr.leboncoin.data.network.api.AlbumApiService
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
    private val albumApiService: AlbumApiService,
    private val albumDatabase: AlbumDatabase,
): AlbumsRepository {

    override suspend fun getAlbums(): AlbumResult<List<Album>> {
        return try {
            val remoteAlbums = albumApiService.getAlbums()
            val localAlbums = albumDatabase.dao.getAlbums()
            albumDatabase.dao.upsertAlbums(
                remoteAlbums.map {
                    val localAlbum = localAlbums.find { localAlbum -> localAlbum.id == it.id }
                    it.toAlbumEntity(localAlbum?.isFavorite ?: false)
                }
            )
            AlbumResult.Success(getAlbumsFromDatabase())
        } catch (throwable: Throwable) {
            when (throwable) {
                is UnknownHostException, is SocketTimeoutException, is IOException -> {
                    AlbumResult.Success(getAlbumsFromDatabase())
                }
                is retrofit2.HttpException -> {
                    AlbumResult.Success(getAlbumsFromDatabase())
                }
                is kotlinx.serialization.SerializationException -> {
                    AlbumResult.Success(getAlbumsFromDatabase())
                }
                else -> {
                    AlbumResult.Failure(AlbumError.Unknown)
                }
            }
        }
    }

    override suspend fun getAlbum(id: Int): AlbumResult<Album> {
        return try {
            val album = albumDatabase.dao.getAlbum(id)
            AlbumResult.Success(album.toAlbum())
        } catch (exception: Exception) {
            AlbumResult.Failure(AlbumError.Unknown)
        }
    }

    override suspend fun updateFavorite(albumId: Int, isFavorite: Boolean) {
        albumDatabase.dao.updateFavorite(albumId, isFavorite)
    }

    private suspend fun getAlbumsFromDatabase(): List<Album> =
        albumDatabase.dao.getAlbums().map { it.toAlbum() }
}