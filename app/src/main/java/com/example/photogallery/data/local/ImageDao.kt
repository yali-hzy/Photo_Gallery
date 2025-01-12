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
    fun getPagedImages(): PagingSource<Int, ImageEntity> // 返回分页数据

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)
}
