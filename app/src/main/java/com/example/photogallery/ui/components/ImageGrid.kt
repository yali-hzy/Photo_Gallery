package com.example.photogallery.ui.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.photogallery.data.local.entities.ImageEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageGrid(images: List<ImageEntity>,
              modifier: Modifier = Modifier,
              onDeleteImage: (String) -> Unit,
              onRenameImage: (String, String) -> Unit,
              onImageClick: (String, String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(images.size) { index ->
            val imageEntity = images[index]
            val showDialog = remember { mutableStateOf(false) }
            val showRenameDialog = remember { mutableStateOf(false) }
            val newName = remember { mutableStateOf(imageEntity.name ?: "") }
            Column(modifier = Modifier.padding(8.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = Uri.parse(imageEntity.uri),
                        onError = { Log.e("ImageError", "Failed to load image: ${imageEntity.uri}") }
                    ),
                    contentDescription = imageEntity.name ?: "未命名",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .combinedClickable(
                            onClick = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onImageClick(imageEntity.uri, imageEntity.name ?: "未命名")
                            },
                            onLongClick = { showDialog.value = true }
                        )
                )
                Text(
                    text = imageEntity.name ?: "未命名",
                    modifier = Modifier.padding(top = 4.dp)
                        .align(alignment = CenterHorizontally)
                )
            }

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("操作选项") },
                    text = {
                        Column {
                            TextButton(onClick = {
                                showDialog.value = false
                                newName.value = imageEntity.name ?: ""
                                showRenameDialog.value = true
                            }) {
                                Text("重命名")
                            }
                            TextButton(onClick = {
                                showDialog.value = false
                                onDeleteImage(imageEntity.uri)
                            }) {
                                Text("删除图片")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text("关闭")
                        }
                    }
                )
            }

            if (showRenameDialog.value) {
                RenameDialog(
                    newName = newName.value,
                    onValueChange = { newName.value = it },
                    onConfirm = {
                        onRenameImage(imageEntity.uri, newName.value)
                        showRenameDialog.value = false
                    },
                    onDismiss = { showRenameDialog.value = false }
                )
            }
        }
    }
}

@Composable
fun RenameDialog(
    newName: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("重命名") },
        text = {
            TextField(
                value = newName,
                onValueChange = onValueChange,
                label = { Text("新名字") }
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}