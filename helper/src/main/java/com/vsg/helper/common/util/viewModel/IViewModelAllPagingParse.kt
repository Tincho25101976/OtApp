package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.helper.progress.ICallbackProcessWithProgress
import com.vsg.helper.ui.data.ILog
import com.vsg.helper.ui.data.IReadToList

interface IViewModelAllPagingParse<TEntity> :
    ICallbackProcessWithProgress<TEntity>,
    IViewModelAllSimpleList<TEntity>,
    ILog,
    IReadToList<TEntity>

        where TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : Comparable<TEntity> {

    fun insert(item: List<TEntity>): Boolean
    fun deleteAll()
}