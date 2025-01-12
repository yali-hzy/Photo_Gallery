package com.example.photogallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
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
                val factory = GalleryViewModelFactory(application)
                val viewModel: GalleryViewModel = viewModel(factory = factory)

                GalleryScreen(
                    images = viewModel.images,
                    onSelectImages = { uris -> viewModel.addImages(uris) }
                )
            }
        }
    }
}
