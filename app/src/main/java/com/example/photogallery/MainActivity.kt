package com.example.photogallery

import android.app.Application
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.photogallery.ui.screens.FullScreenImageScreen
import com.example.photogallery.ui.screens.GalleryScreen
import com.example.photogallery.ui.theme.PhotoGalleryTheme
import com.example.photogallery.viewmodel.GalleryViewModel
import com.example.photogallery.viewmodel.GalleryViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoGalleryTheme {
                PhotoGalleryApp(application = application)
            }
        }
    }
}

@Composable
fun PhotoGalleryApp(application: Application) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "gallery") {
        composable("gallery") {
            val factory = GalleryViewModelFactory(application)
            val viewModel: GalleryViewModel = viewModel(factory = factory)

            GalleryScreen(
                viewModel = viewModel,
                onImageClick = { uri, name ->
                    navController.navigate("fullscreen?uri=${Uri.encode(uri)}&name=$name")
                }
            )
        }

        composable(
            route = "fullscreen?uri={uri}&name={name}",
            arguments = listOf(
                navArgument("uri") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val uri = backStackEntry.arguments?.getString("uri") ?: return@composable
            val name = backStackEntry.arguments?.getString("name") ?: return@composable
            FullScreenImageScreen(uri = uri, name = name) { navController.popBackStack() }
        }
    }
}