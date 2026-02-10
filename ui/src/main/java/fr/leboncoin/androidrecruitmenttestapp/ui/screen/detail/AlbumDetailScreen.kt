package fr.leboncoin.androidrecruitmenttestapp.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.chips.ChipTinted
import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.ErrorScreen
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.LoadingScreen

@Composable
fun AlbumDetailScreen(
    albumId: Int,
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = albumId) {
        viewModel.init(albumId)
    }

    when{
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen()
        else -> {
            state.album?.let {
                UIContent(
                    album = it,
                    onBackClick = onBackClick,
                    onFavoriteClick = {
                        viewModel.updateFavorite(albumId = it.id, isFavorite = !it.isFavorite)
                    }
                )
            } ?: ErrorScreen()
        }
    }
}

@Composable
private fun UIContent(
    album: Album,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.url)
                    .httpHeaders(
                        NetworkHeaders.Builder()
                            .add("User-Agent", "LeboncoinApp/1.0")
                            .build()
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = album.title,
                modifier = Modifier
                    .aspectRatio(350f/250f),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipTinted(
                    text = album.albumTag,
                )
                ChipTinted(
                    text = album.trackTag,
                )
            }
            Text(
                modifier = Modifier.padding(16.dp),
                text = album.title,
                style = SparkTheme.typography.display3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier,
                onClick = {
                    onBackClick()
                }
            ){
                Icon(
                    painter = rememberVectorPainter(
                        image = Icons.AutoMirrored.Outlined.KeyboardArrowLeft
                    ),
                    contentDescription = "Localized description",
                )
            }

            IconButton(
                modifier = Modifier,
                onClick = { onFavoriteClick() }
            ){
                Icon(
                    painter = rememberVectorPainter(
                        image = if (album.isFavorite)
                            Icons.Outlined.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                    ),
                    contentDescription = "Localized description",
                )
            }
        }
    }
}