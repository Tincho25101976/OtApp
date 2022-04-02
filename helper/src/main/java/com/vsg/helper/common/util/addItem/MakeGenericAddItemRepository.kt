package com.vsg.helper.common.util.addItem

import androidx.lifecycle.LiveData
import com.vsg.helper.common.util.MakeGenericRepository
import kotlinx.coroutines.runBlocking

abstract class MakeGenericAddItemRepository<TDao, TEntity>(value: TDao?) :
    MakeGenericRepository<TDao, TEntity>(value),
    IAddItemRepository<TEntity>
        where TDao : IAddItemDao<TEntity>,
              TEntity : ItemBaseAddItem {
    override fun repositoryAny(id: Int): Boolean = runBlocking {
        return@runBlocking dao?.any(id) ?: false
    }

    override fun repositoryViewAll(id: Int): LiveData<List<TEntity>>? = runBlocking {
        return@runBlocking dao?.viewAll(id)
    }

    override fun repositoryViewDefault(id: Int): LiveData<List<TEntity>>? = runBlocking {
        return@runBlocking dao?.viewDefault(id)
    }
}