package com.example.photogallery.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenImageScreen(uri: String, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val inputStream = try {
        context.contentResolver.openInputStream(Uri.parse(uri))?.use { it }
    } catch (e: Exception) {
        Log.e("FullScreenImageScreen", "无法打开 URI: $uri", e)
        null
    }
    if (inputStream == null) {
        Text("图片加载失败，无法访问 URI")
        return
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("图片详情") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) {
        Image(
            painter = rememberAsyncImagePainter(Uri.parse(uri)),
            contentDescription = "全屏图片",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .wrapContentSize()
                .padding(bottom = 40.dp)
        )
    }
}