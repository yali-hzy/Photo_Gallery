package com.example.photogallery.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photogallery.data.local.entities.ImageEntity

@Dao
interface ImageDao {
    @Query("SELECT * FROM images ORDER BY timestamp DESC")
    fun getPagedImages(): PagingSource<Int, ImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)

    @Query("DELETE FROM images WHERE uri = :uri")
    suspend fun deleteImageByUri(uri: String)

    @Query("UPDATE images SET name = :newName WHERE uri = :uri")
    suspend fun updateImageName(uri: String, newName: String)
}
