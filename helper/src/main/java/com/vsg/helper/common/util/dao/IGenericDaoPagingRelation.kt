package com.vsg.helper.common.util.dao

import androidx.paging.DataSource
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled

interface IGenericDaoPagingRelation<T> : IGenericDao<T> where T : IResultRecyclerAdapter,
                                                              T : IEntityPagingLayoutPosition,
                                                              T : IEntity,
                                                              T : IIsEnabled,
                                                              T : Comparable<T> {
    fun viewAllPaging(idRelation: Int): DataSource.Factory<Int, T>
    fun viewAllPaging(): DataSource.Factory<Int, T>
}