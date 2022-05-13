package com.vsg.helper.common.util.dao

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.helper.progress.ICallbackProcessWithProgress
import com.vsg.helper.ui.data.ILog
import com.vsg.helper.ui.data.IReadToList

interface IGenericDaoPagingParse<T> : IGenericDaoPaging<T>,
    ICallbackProcessWithProgress<T>,
    ILog,
    IReadToList<T>,
    IDaoAllSimpleList<T>
        where T : IResultRecyclerAdapter,
              T : IEntityPagingLayoutPosition,
              T : IEntity,
              T : IIsEnabled,
              T : Comparable<T> {
    fun insert(item: List<T>): Boolean
    fun deleteAll()
}