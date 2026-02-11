package fr.leboncoin.androidrecruitmenttestapp.ui.detail

import app.cash.turbine.test
import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.usecase.GetAlbumUseCase
import fr.leboncoin.androidrecruitmenttestapp.domain.usecase.UpdateFavoriteAlbumUseCase
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumError
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.detail.AlbumDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumDetailViewModelTest {

    private val getAlbumUseCase: GetAlbumUseCase = GetAlbumUseCase(mock())
    private val updateFavoriteUseCase: UpdateFavoriteAlbumUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AlbumDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AlbumDetailViewModel(updateFavoriteUseCase, getAlbumUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init load album and update state to success`() = runTest {
        // Given
        val albumId = 1
        val expectedAlbum = Album(
            id = albumId, title = "Album 1",
            url = "url", thumbnailUrl = "thumb", albumTag = "",
            trackTag = "", isFavorite = true
        )
        `when`(getAlbumUseCase(albumId))
            .thenReturn(AlbumResult.Success(expectedAlbum))

        // When
        viewModel.init(albumId)

        // Then
        viewModel.state.test {
            assertEquals(true, awaitItem().isLoading)

            // Success state
            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(expectedAlbum, successState.album)
            assertEquals(true, successState.isFavorite)
            assertEquals(null, successState.error)
        }
    }

    @Test
    fun `init update state to error when album fetch fails`() = runTest {
        // Given
        val albumId = 1
        `when`(getAlbumUseCase(albumId))
            .thenReturn(AlbumResult.Failure(AlbumError.Unknown))

        // When
        viewModel.init(albumId)

        // Then
        viewModel.state.test {
            assertEquals(true, awaitItem().isLoading)

            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals("Unknown", errorState.error)
            assertEquals(null, errorState.album)
        }
    }

    @Test
    fun `updateFavorite call use case and update isFavorite local state`() = runTest {
        // Given
        val albumId = 100
        val isFavorite = true
        `when`(updateFavoriteUseCase(albumId, isFavorite)).thenReturn(Unit)

        // When
        viewModel.updateFavorite(albumId, isFavorite)

        // Then
        viewModel.state.test {
            // Skip initial state
            awaitItem()

            // Verify favorite state update
            val updatedState = awaitItem()
            assertEquals(isFavorite, updatedState.isFavorite)
        }
        verify(updateFavoriteUseCase).invoke(albumId, isFavorite)
    }
}