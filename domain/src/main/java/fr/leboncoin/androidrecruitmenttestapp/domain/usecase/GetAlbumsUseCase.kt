package fr.leboncoin.androidrecruitmenttestapp.domain.usecase

import fr.leboncoin.androidrecruitmenttestapp.domain.repository.AlbumsRepository
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val albumsRepository: AlbumsRepository,
) {
    suspend operator fun invoke() = albumsRepository.getAlbums()
}