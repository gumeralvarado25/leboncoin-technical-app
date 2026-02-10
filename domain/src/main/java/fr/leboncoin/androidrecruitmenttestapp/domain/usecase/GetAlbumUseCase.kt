package fr.leboncoin.androidrecruitmenttestapp.domain.usecase

import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.domain.repository.AlbumsRepository
import fr.leboncoin.androidrecruitmenttestapp.domain.utils.AlbumResult
import javax.inject.Inject

class GetAlbumUseCase @Inject constructor(
    private val albumsRepository: AlbumsRepository,
) {
    suspend operator fun invoke(id: Int): AlbumResult<Album> = albumsRepository.getAlbum(id)
}
