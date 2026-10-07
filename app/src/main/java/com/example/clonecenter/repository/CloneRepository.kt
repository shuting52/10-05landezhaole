package com.example.clonecenter.repository

import android.content.Context
import com.example.clonecenter.database.CloneDatabase
import com.example.clonecenter.database.CloneEntity
import com.example.clonecenter.model.InstalledApp
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class CloneRepository(
    context: Context
) {
    private val dao = CloneDatabase
        .get(context)
        .cloneDao()

    fun observeClones(): Flow<List<CloneEntity>> {
        return dao.observeAll()
    }

    /** 为应用创建分身记录（落库），返回记录 id */
    suspend fun createRecord(
        app: InstalledApp
    ): String {
        val id = UUID.randomUUID().toString()

        dao.insert(
            CloneEntity(
                id = id,
                packageName = app.packageName,
                appLabel = app.label,
                profileUserId = null,
                state = "CREATED",
                createdAt = System.currentTimeMillis()
            )
        )

        return id
    }

    suspend fun updateState(
        id: String,
        state: String,
        profileUserId: Int?
    ) {
        dao.updateState(
            id,
            state,
            profileUserId
        )
    }
}
