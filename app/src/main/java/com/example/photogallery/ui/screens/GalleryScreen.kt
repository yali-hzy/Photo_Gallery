package com.example.photogallery.ui.screens

import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.photogallery.ui.components.ImageGrid
import com.example.photogallery.ui.components.MySearchBar
import com.example.photogallery.ui.components.SelectImagesButton
import com.example.photogallery.viewmodel.GalleryViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel,
    onImageClick: (String) -> Unit
) {
    val images = viewModel.filteredImages.collectAsLazyPagingItems()
    val context = LocalContext.current
    val searchQuery by viewModel.searchQueryState.collectAsState(initial = "")
    val keyboardController = LocalSoftwareKeyboardController.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val contentResolver = context.contentResolver
            for (uri in uris) {
                try {
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } catch (e: SecurityException) {
                    Log.e("PermissionError", "Failed to persist URI permission: $uri", e)
                }
            }
            viewModel.addImages(uris.map { it.toString() })
        }
    }

    BackHandler(enabled = searchQuery.isNotEmpty()) {
        viewModel.setSearchQuery("")
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { androidx.compose.material3.Text(text = "图库") }
                )
                MySearchBar(
                    query = searchQuery,
                    onQueryChange = { query -> viewModel.setSearchQuery(query) }
                )
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .pointerInput(Unit) {
                        coroutineScope {
                            launch {
                                detectTapGestures(onTap = {
                                    keyboardController?.hide()
                                })
                            }
                        }
                    }
            ) {
                Column{
                    ImageGrid(
                        images = images.itemSnapshotList.items,
                        onDeleteImage = { uri -> viewModel.deleteImage(uri) },
                        onRenameImage = { uri, newName -> viewModel.updateImageName(uri, newName) },
                        onImageClick = onImageClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    SelectImagesButton(
                        onClick = { launcher.launch(arrayOf("image/*")) }
                    )
                }
            }
        }
    )
}