package fr.leboncoin.androidrecruitmenttestapp.domain.usecase

import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.repository.AlbumsRepository
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(
    private val albumsRepository: AlbumsRepository,
) {
    suspend operator fun invoke(): AlbumResult<List<Album>> = albumsRepository.getAlbums()
}