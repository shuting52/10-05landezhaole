package com.example.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadedResourceDao {
    @Query("SELECT * FROM uploaded_resources WHERE type = :type ORDER BY timestamp DESC")
    fun getResourcesByType(type: String): Flow<List<UploadedResourceEntity>>

    @Query("SELECT * FROM uploaded_resources WHERE type = 'prompt_image' OR type = 'prompt_video' ORDER BY timestamp DESC")
    fun getPrompts(): Flow<List<UploadedResourceEntity>>

    @Query("SELECT * FROM uploaded_resources ORDER BY timestamp DESC")
    fun getAllResources(): Flow<List<UploadedResourceEntity>>

    @Query("SELECT * FROM uploaded_resources")
    suspend fun getAllResourcesOnce(): List<UploadedResourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResource(resource: UploadedResourceEntity)

    @Query("DELETE FROM uploaded_resources WHERE id = :id")
    suspend fun deleteResource(id: String)
}
