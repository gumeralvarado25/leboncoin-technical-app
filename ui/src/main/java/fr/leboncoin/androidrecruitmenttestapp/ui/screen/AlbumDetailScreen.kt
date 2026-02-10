package fr.leboncoin.androidrecruitmenttestapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.chips.ChipTinted

@Composable
fun AlbumDetailScreen(
    albumTitle: String,
    albumUrl: String,
    albumTag: String,
    trackTag: String,
    modifier: Modifier = Modifier,
) {
    Column {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(albumUrl)
                .httpHeaders(
                    NetworkHeaders.Builder()
                        .add("User-Agent", "LeboncoinApp/1.0")
                        .build()
                )
                .crossfade(true)
                .build(),
            contentDescription = albumTitle,
            modifier = modifier
                .aspectRatio(350f/250f),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChipTinted(
                text = albumTag,
            )
            ChipTinted(
                text = trackTag,
            )
        }
        Text(
            modifier = Modifier.padding(16.dp),
            text = albumTitle,
            style = SparkTheme.typography.display3,
            overflow = TextOverflow.Ellipsis
        )
    }
}