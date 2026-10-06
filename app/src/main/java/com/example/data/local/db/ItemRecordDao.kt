package com.example.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemRecordDao {
    @Query("SELECT * FROM user_records WHERE isFavorite = 1 ORDER BY visitedTimestamp DESC")
    fun getFavorites(): Flow<List<UserItemRecord>>

    @Query("SELECT * FROM user_records WHERE visitedTimestamp > 0 ORDER BY visitedTimestamp DESC LIMIT 100")
    fun getHistory(): Flow<List<UserItemRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: UserItemRecord)

    @Query("SELECT * FROM user_records WHERE url = :url LIMIT 1")
    suspend fun getByUrl(url: String): UserItemRecord?

    @Query("UPDATE user_records SET isFavorite = :isFav WHERE url = :url")
    suspend fun updateFavorite(url: String, isFav: Boolean)

    @Query("DELETE FROM user_records WHERE isFavorite = 0")
    suspend fun deleteNonFavorites()

    @Query("UPDATE user_records SET visitedTimestamp = 0, visitCount = 0 WHERE isFavorite = 1")
    suspend fun clearHistoryKeepFavorites()
}
