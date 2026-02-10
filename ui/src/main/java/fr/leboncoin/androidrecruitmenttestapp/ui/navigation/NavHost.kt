package fr.leboncoin.androidrecruitmenttestapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.AlbumsScreen
import fr.leboncoin.androidrecruitmenttestapp.ui.screen.detail.AlbumDetailScreen

@Composable
fun MainNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "albumList") {
        composable("albumList") {
            AlbumsScreen(onItemSelected = { album ->
                navController.navigate(
                    route = "albumDetail/${album.id}"
                )
            })
        }

        composable(
            route = "albumDetail/{albumId}",
            arguments = listOf(
                navArgument("albumId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId") ?: 0

            AlbumDetailScreen(
                albumId = albumId,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}