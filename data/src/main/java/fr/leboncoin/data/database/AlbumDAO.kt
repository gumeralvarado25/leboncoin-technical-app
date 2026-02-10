package fr.leboncoin.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AlbumDAO {
    @Query("SELECT * FROM AlbumEntity")
    suspend fun getAlbums(): List<AlbumEntity>

    @Upsert
    suspend fun upsertAlbums(albums: List<AlbumEntity>)

    @Query("DELETE FROM AlbumEntity")
    suspend fun clearAlbums()

    @Query("UPDATE AlbumEntity SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT * FROM AlbumEntity WHERE id = :id")
    suspend fun getAlbum(id: Int): AlbumEntity
}