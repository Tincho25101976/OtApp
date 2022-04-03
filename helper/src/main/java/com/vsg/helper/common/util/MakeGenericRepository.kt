package com.vsg.helper.common.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IGenericDao
import kotlinx.coroutines.runBlocking

abstract class MakeGenericRepository<TDao, TEntity>(protected val dao: TDao?)
        where TDao : IGenericDao<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled {
    var isDao: Boolean = (dao != null)

    open fun repositoryInsert(item: TEntity): Long =
        runBlocking { return@runBlocking dao?.insert(item) ?: RESULT_NULL_LONG }

    open fun repositoryUpdate(item: TEntity): Int =
        runBlocking { dao?.update(item) ?: RESULT_NULL_INT }

    open fun repositoryDelete(item: TEntity): Int =
        runBlocking { return@runBlocking dao?.delete(item) ?: RESULT_NULL_INT }

    // Queries:
    open fun repositoryView(id: Int): TEntity? =
        runBlocking { return@runBlocking dao?.view(id) }

    open fun repositoryView(id: TEntity): TEntity? =
        runBlocking { return@runBlocking dao?.view(id.id) ?: RESULT_NULL_ENTITY }

    open fun repositoryViewAll(): LiveData<List<TEntity>> = runBlocking {
        return@runBlocking dao?.viewAll() ?: MutableLiveData()
    }

    companion object {
        const val RESULT_NULL_INT: Int = -1
        const val RESULT_NULL_LONG: Long = -1
        val RESULT_NULL_ENTITY = null
    }
}