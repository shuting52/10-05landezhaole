package com.example.clonecenter.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CloneDao {

    @Query(
        """
        SELECT * FROM clone_instances
        ORDER BY createdAt DESC
    """
    )
    fun observeAll(): Flow<List<CloneEntity>>

    @Query(
        """
        SELECT * FROM clone_instances
        WHERE packageName = :packageName
    """
    )
    suspend fun findByPackage(
        packageName: String
    ): List<CloneEntity>

    @Insert
    suspend fun insert(entity: CloneEntity)

    @Delete
    suspend fun delete(entity: CloneEntity)

    @Query(
        """
        UPDATE clone_instances
        SET state = :state,
            profileUserId = :profileUserId
        WHERE id = :id
    """
    )
    suspend fun updateState(
        id: String,
        state: String,
        profileUserId: Int?
    )
}
