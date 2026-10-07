package com.example.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CloneAppDao {
    @Query("SELECT * FROM clone_apps ORDER BY createdAt DESC")
    fun getAllClones(): Flow<List<CloneAppEntity>>

    @Query("SELECT * FROM clone_apps WHERE packageName = :packageName ORDER BY cloneIndex ASC")
    fun getClonesForPackage(packageName: String): Flow<List<CloneAppEntity>>

    @Query("SELECT MAX(cloneIndex) FROM clone_apps WHERE packageName = :packageName")
    suspend fun getMaxCloneIndex(packageName: String): Int?

    @Query("SELECT COUNT(*) FROM clone_apps WHERE packageName = :packageName")
    suspend fun getCloneCount(packageName: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClone(clone: CloneAppEntity)

    @Update
    suspend fun updateClone(clone: CloneAppEntity)

    @Delete
    suspend fun deleteClone(clone: CloneAppEntity)

    @Query("DELETE FROM clone_apps WHERE id = :id")
    suspend fun deleteCloneById(id: String)

    @Query("UPDATE clone_apps SET launchCount = launchCount + 1, lastLaunchAt = :timestamp WHERE id = :id")
    suspend fun recordLaunch(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE clone_apps SET cloneName = :newName WHERE id = :id")
    suspend fun renameClone(id: String, newName: String)
}
