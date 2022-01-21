package com.vsg.helper.common.util.addItem

import androidx.lifecycle.LiveData

interface IAddItemRepository<TEntity> where TEntity : ItemBaseAddItem {
    fun repositoryAny(id: Long): Boolean
    fun repositoryViewAll(id: Long): LiveData<List<TEntity>>?
    fun repositoryViewDefault(id: Long): LiveData<List<TEntity>>?
}