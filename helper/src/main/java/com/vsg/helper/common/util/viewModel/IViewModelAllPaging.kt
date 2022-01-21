package com.vsg.helper.common.util.viewModel

import androidx.paging.PagingData
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import kotlinx.coroutines.flow.Flow

interface IViewModelAllPaging<TEntity>
        where TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {
    fun viewModelGetViewAllPaging(): Flow<PagingData<TEntity>>
}