package fr.lebocoin.data

import fr.leboncoin.androidrecruitmenttestapp.domain.repository.AlbumsRepository
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumError
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import fr.leboncoin.data.database.AlbumDAO
import fr.leboncoin.data.database.AlbumDatabase
import fr.leboncoin.data.database.AlbumEntity
import fr.leboncoin.data.mapper.toAlbum
import fr.leboncoin.data.mapper.toAlbumEntity
import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.repository.AlbumsRepositoryImpl
import fr.leboncoin.data.response.AlbumDto
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import retrofit2.HttpException
import retrofit2.Response

class AlbumRepositoryTest {

    @Mock
    private lateinit var albumService: AlbumApiService

    private lateinit var albumDatabase: AlbumDatabase

    @Mock
    private lateinit var albumDAO: AlbumDAO

    private lateinit var albumRepository: AlbumsRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        albumDatabase = mock<AlbumDatabase> {
            on { dao }.thenReturn(albumDAO)
        }
        albumRepository = AlbumsRepositoryImpl(albumService, albumDatabase)
    }

    @Test
    fun `getAlbums returns Success with albums from database when service fails with UnknownHostException`() = runTest {
        // Given
        val expectedAlbumEntities = listOf(
            AlbumEntity(
                id = 1,
                albumId = 1,
                title = "title1",
                url = "url1",
                thumbnailUrl = "https://via.placeholder.com/150/6ffa50",
                albumTag = "",
                trackTag = "",
                isFavorite = false,
            ),
            AlbumEntity(
                id = 2,
                albumId = 2,
                title = "title2",
                url = "url2",
                thumbnailUrl = "https://via.placeholder.com/150/6ffa50",
                albumTag = "",
                trackTag = "",
                isFavorite = false,
            )
        )

        val expectedAlbums = expectedAlbumEntities.map { it.toAlbum() }

        `when`(albumService.getAlbums())
            .thenThrow(
                HttpException(Response.error<Any>(
                    404,
                    "".toResponseBody(null)
                ))
            )
        `when`(albumDatabase.dao.getAlbums()).thenReturn(expectedAlbumEntities)

        // When
        val result = albumRepository.getAlbums()

        // Then
        assertEquals(AlbumResult.Success(expectedAlbums), result)
    }

    @Test
    fun `getAlbums returns Failure when service fails with other exception`() = runTest {
        // Given
        `when`(albumService.getAlbums()).thenThrow(RuntimeException())

        // When
        val result = albumRepository.getAlbums()

        // Then
        assertEquals(AlbumError.Unknown, (result as AlbumResult.Failure).error)
    }

    @Test
    fun `getAlbums returns Success with albums from service and updates database`() = runTest {
        // Given
        val expectedAlbumsDTO = listOf(
            AlbumDto(1, 1, "title1", "url1", "https://via.placeholder.com/150/6ffa50"),
            AlbumDto(2, 2, "title2", "url2", "https://via.placeholder.com/150/6ffa50")
        )
        val expectedAlbumEntities = expectedAlbumsDTO.map { it.toAlbumEntity(true) }
        val expectedAlbums = expectedAlbumEntities.map { it.toAlbum() }

        `when`(albumService.getAlbums()).thenReturn(expectedAlbumsDTO)
        `when`(albumDAO.getAlbums()).thenReturn(expectedAlbumEntities)

        // When
        val result = albumRepository.getAlbums()

        // Then
        assertEquals(AlbumResult.Success(expectedAlbums), result)
    }

    @Test
    fun `getAlbum returns Success with album from database`() = runTest {
        // Given
        val albumId = 1
        val expectedAlbumEntity = AlbumEntity(
            id = 1,
            title = "title1",
            url = "url1",
            thumbnailUrl = "",
            albumTag = "",
            trackTag = "",
            isFavorite = false,
            albumId = 1,
        )
        val expectedAlbum = expectedAlbumEntity.toAlbum()

        `when`(albumDatabase.dao.getAlbum(albumId)).thenReturn(expectedAlbumEntity)

        // When
        val result = albumRepository.getAlbum(albumId)

        // Then
        assertEquals(AlbumResult.Success(expectedAlbum), result)
    }

    @Test
    fun `getAlbum returns Failure when database fails`() = runTest {
        // Given
        val albumId = 1
        `when`(albumDatabase.dao.getAlbum(albumId)).thenThrow(IllegalStateException())

        // When
        val result = albumRepository.getAlbum(albumId)

        // Then
        assertEquals(AlbumError.Unknown, (result as AlbumResult.Failure).error)
    }

    @Test
    fun `verify updateFavorite is called when updating favorite in database`() = runTest {
        // Given
        val albumId = 1
        val isFavorite = false

        // When
        albumRepository.updateFavorite(albumId, isFavorite)

        // Then
        verify(albumDatabase.dao).updateFavorite(albumId, isFavorite)
    }
}