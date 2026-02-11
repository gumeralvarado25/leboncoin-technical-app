package fr.leboncoin.androidrecruitmenttestapp.ui

import app.cash.turbine.test
import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.usecase.GetAlbumsUseCase
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumError
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.AlbumsViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    private val getAlbumsUseCase: GetAlbumsUseCase = GetAlbumsUseCase(mock())
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init update state to success when use case returns data`() = runTest {
        // Given
        val expectedAlbums = listOf(
            Album(
                id = 1,
                title = "Album 1",
                url = "",
                thumbnailUrl = "",
                albumTag = "",
                trackTag = "",
                isFavorite = false,
            )
        )
        `when`(getAlbumsUseCase())
            .thenReturn(AlbumResult.Success(expectedAlbums))

        // When
        val viewModel = AlbumsViewModel(getAlbumsUseCase)

        // Then
        viewModel.state.test {
            assertEquals(true, awaitItem().isLoading)

            // Success state check
            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(expectedAlbums, successState.albums)
            assertEquals(null, successState.error)
        }
    }

    @Test
    fun `init update state to failure when use case returns error`() = runTest {
        // Given
        val error = AlbumError.Unknown
        `when`(getAlbumsUseCase()).thenReturn(AlbumResult.Failure(error))

        // When
        val viewModel = AlbumsViewModel(getAlbumsUseCase)

        // Then
        viewModel.state.test {
            assertEquals(true, awaitItem().isLoading)

            val failureState = awaitItem()
            assertEquals(false, failureState.isLoading)
            assertEquals(error.toString(), failureState.error)
            assertEquals(emptyList<Album>(), failureState.albums)
        }
    }
}