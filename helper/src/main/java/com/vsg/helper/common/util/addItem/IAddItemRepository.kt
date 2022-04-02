package com.vsg.helper.common.util.addItem

import androidx.lifecycle.LiveData

interface IAddItemRepository<TEntity> where TEntity : ItemBaseAddItem {
    fun repositoryAny(id: Int): Boolean
    fun repositoryViewAll(id: Int): LiveData<List<TEntity>>?
    fun repositoryViewDefault(id: Int): LiveData<List<TEntity>>?
}