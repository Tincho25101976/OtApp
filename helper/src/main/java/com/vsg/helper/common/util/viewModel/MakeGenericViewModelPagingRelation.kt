package com.vsg.helper.common.util.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.*
import kotlinx.coroutines.runBlocking

abstract class MakeGenericViewModelPagingRelation<TDao, TEntity>(
    dao: TDao,
    context: Application
) : MakeGenericViewModel<TDao, TEntity>(dao, context),
    IViewModelUpdateSetEnabled<TEntity>,
    IViewModelAllPagingRelation<TEntity>,
    IViewModelAllSimpleListIdRelation<TEntity>,
    IViewModelAllTextSearchRelation
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPagingRelation<TEntity>,
              TDao : IDaoAllTextSearchRelation,
              TDao : IDaoAllSimpleListRelation<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {

    override fun viewModelGetViewAllPaging(idRelation: Long) = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewAllPaging(idRelation).asPagingSourceFactory()
        ).flow
    }

    override fun viewModelGetAllTextSearch(idRelation: Long): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewGetAllTextSearch(idRelation)
    }

    override fun viewModelViewAllSimpleList(idRelation: Long): List<TEntity> =
        dao.viewAllSimpleList(idRelation) ?: listOf()
}