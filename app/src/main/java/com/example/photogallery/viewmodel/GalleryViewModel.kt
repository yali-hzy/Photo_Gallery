package com.example.photogallery.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.photogallery.classifier.ImageClassifier
import com.example.photogallery.data.local.AppDatabase
import com.example.photogallery.data.local.entities.ImageEntity
import com.example.photogallery.data.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ImageRepository
    private val imageClassifier: ImageClassifier

    init {
        val db = AppDatabase.getInstance(application)
        repository = ImageRepository(db.imageDao())
        imageClassifier = ImageClassifier(application)
    }

    val images: Flow<PagingData<ImageEntity>> = repository.getImages().cachedIn(viewModelScope)

    fun addImages(uris: List<String>) {
        val timestamp = System.currentTimeMillis()
        val newImages = uris.map { uri ->
            val name = imageClassifier.classifyImage(Uri.parse(uri))
            ImageEntity(uri = uri, name = name, timestamp = timestamp)
        }
        Log.e("GalleryViewModel", "addImages: $newImages")
        viewModelScope.launch {
            repository.addImages(newImages)
        }
    }

    fun deleteImage(uri: String) {
        viewModelScope.launch {
            repository.deleteImageByUri(uri)
        }
    }

    fun updateImageName(uri: String, newName: String) {
        viewModelScope.launch {
            repository.updateImageName(uri, newName)
        }
    }

    override fun onCleared() {
        super.onCleared()
        imageClassifier.close() // 释放资源
    }
}
