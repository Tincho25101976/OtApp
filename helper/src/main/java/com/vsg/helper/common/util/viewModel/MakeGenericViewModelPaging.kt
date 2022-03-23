package com.vsg.helper.common.util.viewModel

import android.app.Application
import androidx.paging.Pager
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.model.viewModel.IViewModelStoredMap
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPaging
import kotlinx.coroutines.runBlocking

abstract class MakeGenericViewModelPaging<TDao, TEntity>(
    dao: TDao,
    context: Application,
    stored: IViewModelStoredMap
) : MakeGenericViewModel<TDao, TEntity>(dao, context, stored),
    IViewModelUpdateSetEnabled<TEntity>,
    IViewModelAllPaging<TEntity>
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPaging<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {
    //region paging
    override fun viewModelGetViewAllPaging() = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewAllPaging().asPagingSourceFactory()
        ).flow
    }
    //endregion

}