package com.vsg.helper.common.util.addItem

import androidx.lifecycle.LiveData
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.util.dao.IGenericDao

interface IAddItemDao<TEntity> : IGenericDao<TEntity>
        where TEntity : ItemBase,
              TEntity : IAddItemEntity {
    fun viewAll(idRelation: Int): LiveData<List<TEntity>>
    fun any(idRelation: Int): Boolean
    fun viewDefault(idRelation: Int): LiveData<List<TEntity>>
}