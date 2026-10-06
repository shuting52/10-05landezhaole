package com.example.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserItemRecord::class, UploadedResourceEntity::class, CloneAppEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemRecordDao(): ItemRecordDao
    abstract fun uploadedResourceDao(): UploadedResourceDao
    abstract fun cloneAppDao(): CloneAppDao

    companion object {
        // v3 -> v4：为 uploaded_resources 增加 fileUrl / fileType 列（保留用户收藏与历史数据）
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE uploaded_resources ADD COLUMN fileUrl TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE uploaded_resources ADD COLUMN fileType TEXT NOT NULL DEFAULT ''")
            }
        }

        // v4 -> v5：增加 prompt / previewUrl / iconUrl / mediaUrl / mode 列（提示词区与图标）
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE uploaded_resources ADD COLUMN prompt TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE uploaded_resources ADD COLUMN previewUrl TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE uploaded_resources ADD COLUMN iconUrl TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE uploaded_resources ADD COLUMN mediaUrl TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE uploaded_resources ADD COLUMN mode TEXT NOT NULL DEFAULT 'file'")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lazy_nav_database"
                )
                    .addMigrations(MIGRATION_3_4, MIGRATION_4_5)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
