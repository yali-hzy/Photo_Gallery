package com.example.photogallery.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.photogallery.ui.components.ImageGrid
import com.example.photogallery.ui.components.SelectImagesButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen() {
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        selectedImages = uris
    }

    Scaffold(
        topBar = { androidx.compose.material3.TopAppBar(title = { Text("图库") }) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                ImageGrid(
                    images = selectedImages,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                SelectImagesButton(
                    onClick = { launcher.launch(arrayOf("image/*")) }
                )
            }
        }
    )
}
