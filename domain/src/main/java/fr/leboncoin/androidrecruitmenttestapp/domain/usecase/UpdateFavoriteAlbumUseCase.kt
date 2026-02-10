package fr.leboncoin.androidrecruitmenttestapp.domain.usecase

import fr.leboncoin.androidrecruitmenttestapp.domain.repository.AlbumsRepository
import javax.inject.Inject

class UpdateFavoriteAlbumUseCase @Inject constructor(
    private val albumsRepository: AlbumsRepository
) {
    suspend operator fun invoke(albumId: Int, isFavorite: Boolean) {
        albumsRepository.updateFavorite(albumId, isFavorite)
    }
}