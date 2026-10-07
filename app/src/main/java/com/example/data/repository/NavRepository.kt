package com.example.data.repository

import com.example.data.local.db.CloneAppDao
import com.example.data.local.db.CloneAppEntity
import com.example.data.local.db.ItemRecordDao
import com.example.data.local.db.UploadedResourceDao
import com.example.data.local.db.UploadedResourceEntity
import com.example.data.local.db.UserItemRecord
import com.example.data.model.NavCard
import kotlinx.coroutines.flow.Flow

class NavRepository(
    private val dao: ItemRecordDao,
    private val uploadDao: UploadedResourceDao,
    private val cloneAppDao: CloneAppDao
) {

    val favorites: Flow<List<UserItemRecord>> = dao.getFavorites()
    val history: Flow<List<UserItemRecord>> = dao.getHistory()

    fun getUploadedSoftware(): Flow<List<UploadedResourceEntity>> = uploadDao.getResourcesByType("software")
    fun getUploadedSkills(): Flow<List<UploadedResourceEntity>> = uploadDao.getResourcesByType("skill")
    fun getUploadedPrompts(): Flow<List<UploadedResourceEntity>> = uploadDao.getPrompts()
    fun getCustomSites(): Flow<List<UploadedResourceEntity>> = uploadDao.getResourcesByType("custom_site")
    suspend fun getAllUploadedResources(): List<UploadedResourceEntity> = uploadDao.getAllResourcesOnce()

    suspend fun saveUploadedResource(resource: UploadedResourceEntity) {
        uploadDao.insertResource(resource)
    }

    suspend fun deleteUploadedResource(id: String) {
        uploadDao.deleteResource(id)
    }

    // ============ 分身多开 ============
    fun getAllClones(): Flow<List<CloneAppEntity>> = cloneAppDao.getAllClones()
    fun getClonesForPackage(packageName: String): Flow<List<CloneAppEntity>> = cloneAppDao.getClonesForPackage(packageName)
    suspend fun getMaxCloneIndex(packageName: String): Int? = cloneAppDao.getMaxCloneIndex(packageName)
    suspend fun getCloneCount(packageName: String): Int = cloneAppDao.getCloneCount(packageName)
    suspend fun saveClone(clone: CloneAppEntity) = cloneAppDao.insertClone(clone)
    suspend fun deleteClone(id: String) = cloneAppDao.deleteCloneById(id)
    suspend fun renameClone(id: String, newName: String) = cloneAppDao.renameClone(id, newName)
    suspend fun recordCloneLaunch(id: String) = cloneAppDao.recordLaunch(id)

    suspend fun recordVisit(card: NavCard) {
        val existing = dao.getByUrl(card.url)
        val updated = if (existing != null) {
            existing.copy(
                visitedTimestamp = System.currentTimeMillis(),
                visitCount = existing.visitCount + 1,
                title = card.title,
                badge = card.badge ?: existing.badge,
                desc = card.desc.ifBlank { existing.desc }
            )
        } else {
            UserItemRecord(
                url = card.url,
                title = card.title,
                categoryName = card.categoryId,
                badge = card.badge,
                desc = card.desc,
                iconUrl = card.icon,
                isFavorite = false,
                visitedTimestamp = System.currentTimeMillis(),
                visitCount = 1
            )
        }
        dao.insertOrUpdate(updated)
    }

    suspend fun toggleFavorite(card: NavCard): Boolean {
        val existing = dao.getByUrl(card.url)
        val newFavState = if (existing != null) !existing.isFavorite else true
        val updated = if (existing != null) {
            existing.copy(isFavorite = newFavState)
        } else {
            UserItemRecord(
                url = card.url,
                title = card.title,
                categoryName = card.categoryId,
                badge = card.badge,
                desc = card.desc,
                iconUrl = card.icon,
                isFavorite = true,
                visitedTimestamp = 0L,
                visitCount = 0
            )
        }
        dao.insertOrUpdate(updated)
        return newFavState
    }

    suspend fun isFavorite(url: String): Boolean {
        return dao.getByUrl(url)?.isFavorite == true
    }

    suspend fun clearHistory() {
        dao.clearHistoryKeepFavorites()
        dao.deleteNonFavorites()
    }
}
