package com.example.clonecenter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CloneEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CloneDatabase : RoomDatabase() {

    abstract fun cloneDao(): CloneDao

    companion object {
        @Volatile
        private var INSTANCE: CloneDatabase? = null

        fun get(context: Context): CloneDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CloneDatabase::class.java,
                    "clone_center.db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
