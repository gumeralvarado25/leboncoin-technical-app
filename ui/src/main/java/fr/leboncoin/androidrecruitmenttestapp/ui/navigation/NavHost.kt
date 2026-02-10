package fr.leboncoin.androidrecruitmenttestapp.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.AlbumDetailScreen
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.AlbumsScreen

@Composable
fun MainNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "albumList") {
        composable("albumList") {
            AlbumsScreen(onItemSelected = { album ->
                // Use Uri.encode for URLs to avoid issues with special characters (/ or :)
                val encodedUrl = Uri.encode(album.url)
                navController.navigate("albumDetail/${album.title}/${encodedUrl}/${album.albumTag}/${album.trackTag}")
            })
        }

        composable(
            route = "albumDetail/{title}/{url}/{albumTag}/{trackTag}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
                navArgument("albumTag") { type = NavType.StringType },
                navArgument("trackTag") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Extract arguments
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val url = backStackEntry.arguments?.getString("url") ?: ""
            val albumTag = backStackEntry.arguments?.getString("albumTag") ?: ""
            val trackTag = backStackEntry.arguments?.getString("trackTag") ?: ""

            AlbumDetailScreen(
                albumTitle = title,
                albumUrl = url,
                albumTag = albumTag,
                trackTag = trackTag
            )
        }
    }
}