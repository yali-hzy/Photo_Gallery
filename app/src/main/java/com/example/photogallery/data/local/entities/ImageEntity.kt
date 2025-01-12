package com.example.photogallery.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
    indices = [Index(value = ["uri"], unique = true)]
)
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)
