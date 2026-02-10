package fr.leboncoin.androidrecruitmenttestapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.usecase.GetAlbumUseCase
import fr.leboncoin.androidrecruitmenttestapp.domain.usecase.UpdateFavoriteAlbumUseCase
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val updateFavoriteAlbumUseCase: UpdateFavoriteAlbumUseCase,
    private val getAlbumUseCase: GetAlbumUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(AlbumDetailScreenState())
    val state = _state

    fun init(albumId: Int) {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            when(val album = getAlbumUseCase(albumId)) {
                is AlbumResult.Failure -> {
                    _state.value = _state.value.copy(error = album.error.toString(), album = null, isLoading = false)
                }
                is AlbumResult.Success -> {
                    _state.value = _state.value.copy(album = album.data, isLoading = false, error = null)
                }
            }

        }
    }

    fun updateFavorite(albumId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            updateFavoriteAlbumUseCase(albumId, isFavorite)
            init(albumId)
        }
    }
}

data class AlbumDetailScreenState(
    val isLoading: Boolean = true,
    val album: Album? = null,
    val error: String? = null,
)