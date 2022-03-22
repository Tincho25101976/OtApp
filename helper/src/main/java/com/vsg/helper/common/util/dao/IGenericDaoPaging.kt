package com.vsg.helper.common.util.dao

import androidx.paging.DataSource
import androidx.room.Dao
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled

@Dao
interface IGenericDaoPaging<T> : IGenericDao<T>
        where T : IResultRecyclerAdapter,
              T : IEntityPagingLayoutPosition,
              T : IEntity,
              T : IIsEnabled,
              T : Comparable<T> {
    fun viewAllPaging(): DataSource.Factory<Int, T>

}