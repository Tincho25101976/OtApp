package com.vsg.helper.common.util.addItem

import android.app.Application
import androidx.lifecycle.LiveData
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.model.viewModel.IViewModelStoredMap
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPaging
import com.vsg.helper.common.util.viewModel.IViewModelAllById
import com.vsg.helper.common.util.viewModel.MakeGenericViewModelPaging
import kotlinx.coroutines.runBlocking

abstract class MakeGenericViewModelPagingAddItem<TDao, TEntity>(
    dao: TDao,
    context: Application,
    stored: IViewModelStoredMap
) : MakeGenericViewModelPaging<TDao, TEntity>(dao, context, stored), IViewModelAllById<TEntity>
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPaging<TEntity>,
              TDao : IAddItemDao<TEntity>,
              TEntity : ItemBase,
              TEntity : IAddItemEntity,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {
    override fun viewModelViewAll(id: Int): LiveData<List<TEntity>> = runBlocking {
        return@runBlocking dao.viewAll(id)
    }
}