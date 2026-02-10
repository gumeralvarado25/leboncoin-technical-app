package fr.leboncoin.androidrecruitmenttestapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.SparkTheme
import fr.leboncoin.androidrecruitmenttestapp.domain.entity.Album
import fr.leboncoin.androidrecruitmenttestapp.ui.component.AlbumItem

@Composable
fun AlbumsScreen(
    onItemSelected : (Album) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    UIContent(state = state, modifier = modifier, onItemSelected = onItemSelected)
}

@Composable
private fun UIContent(
    state: AlbumsScreenState,
    onItemSelected : (Album) -> Unit,
    modifier: Modifier = Modifier,
){
    when{
        state.isLoading -> LoadingScreen()
        state.error != null -> ErrorScreen()
        state.albums.isEmpty() -> NoAlbumsScreen()
        else -> AlbumsListUI(
            albums = state.albums,
            onItemSelected = onItemSelected,
            modifier = modifier
        )
    }
}

@Composable
private fun AlbumsListUI(
    albums: List<Album>,
    onItemSelected : (Album) -> Unit,
    modifier: Modifier = Modifier,
){
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Text(
                text = "All Albums",
                style = SparkTheme.typography.display2,
            )
        }
        items(albums) { album ->
            AlbumItem(
                album = album,
                onItemSelected = onItemSelected,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlbumsLoadingScreenPreview(){
    UIContent(
        state = AlbumsScreenState(),
        onItemSelected = {},
    )
}

@Preview(showBackground = true)
@Composable
fun AlbumsErrorScreenPreview(){
    UIContent(
        state = AlbumsScreenState(isLoading = false, error = ""),
        onItemSelected = {},
    )
}

@Preview(showBackground = true)
@Composable
fun AlbumsScreenPreview(){
    UIContent(
        state = AlbumsScreenState(
            isLoading = false,
            albums = listOf(
                Album(title = "title", thumbnailUrl = "thumbnailUrl", albumTag = "Album #1", trackTag = "Track #1", url = ""),
                Album(title = "title", thumbnailUrl = "thumbnailUrl", albumTag = "Album #1", trackTag = "Track #1", url = ""),
                Album(title = "title", thumbnailUrl = "thumbnailUrl", albumTag = "Album #1", trackTag = "Track #1", url = ""),
                Album(title = "title", thumbnailUrl = "thumbnailUrl", albumTag = "Album #1", trackTag = "Track #1", url = ""),
                Album(title = "title", thumbnailUrl = "thumbnailUrl", albumTag = "Album #1", trackTag = "Track #1", url = ""),
            )
        ),
        onItemSelected = {},
    )
}