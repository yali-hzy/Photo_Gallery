package com.example.photogallery.ui.screens

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.photogallery.data.local.entities.ImageEntity
import com.example.photogallery.ui.components.ImageGrid
import com.example.photogallery.ui.components.SelectImagesButton
import com.example.photogallery.viewmodel.GalleryViewModel
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel,
    onImageClick: (String) -> Unit
) {
    val images: Flow<PagingData<ImageEntity>> = viewModel.images
    val lazyImages = images.collectAsLazyPagingItems()
    val context = LocalContext.current

    // 图片选择器 Launcher
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

    Scaffold(
        topBar = { TopAppBar(title = { Text("图库") }) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                val imageEntities = lazyImages.itemSnapshotList.items
                ImageGrid(
                    images = imageEntities,
                    onDeleteImage = { uri -> viewModel.deleteImage(uri) },
                    onRenameImage = { uri, newName -> viewModel.updateImageName(uri, newName) },
                    onImageClick = onImageClick,
                    modifier = Modifier.weight(1f)
                        .fillMaxWidth()
                )

                SelectImagesButton(
                    onClick = { launcher.launch(arrayOf("image/*")) }
                )
            }
        }
    )
}
