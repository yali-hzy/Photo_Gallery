package com.example.photogallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.photogallery.data.local.ImageDao
import com.example.photogallery.data.local.entities.ImageEntity
import kotlinx.coroutines.flow.Flow

class ImageRepository(private val imageDao: ImageDao) {
    fun getImages(): Flow<PagingData<ImageEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { imageDao.getPagedImages() }
        ).flow
    }

    suspend fun addImages(images: List<ImageEntity>) {
        imageDao.insertImages(images)
    }

    suspend fun deleteImageByUri(uri: String) {
        imageDao.deleteImageByUri(uri)
    }

    suspend fun updateImageName(uri: String, newName: String) {
        imageDao.updateImageName(uri, newName)
    }
}
