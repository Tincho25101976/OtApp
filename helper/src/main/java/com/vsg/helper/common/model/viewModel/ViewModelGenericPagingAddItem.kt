package com.vsg.helper.common.model.viewModel

import android.app.Application
import androidx.paging.Pager
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.util.addItem.IAddItemDao
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.helper.common.util.addItem.MakeGenericViewModelPagingAddItem
import com.vsg.helper.common.util.dao.IDaoAllSimpleListRelation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPaging
import com.vsg.helper.common.util.dao.IGenericDaoPagingRelation
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KType

@ExperimentalStdlibApi
abstract class ViewModelGenericPagingAddItem<TDao, TEntity>(dao: TDao, context: Application,  stored: IViewModelStoredMap) :
    MakeGenericViewModelPagingAddItem<TDao, TEntity>(dao, context, stored),
    IViewModelAllSimpleListIdRelation<TEntity>
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPaging<TEntity>,
              TDao : IAddItemDao<TEntity>,
              TDao : IGenericDaoPagingRelation<TEntity>,
              TDao : IDaoAllSimpleListRelation<TEntity>,
              TEntity : ItemBase,
              TEntity : IAddItemEntity,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {
    //region IViewModel
    override fun getInstanceOfIViewModelView(type: KType): IViewModelView<*>? {
        return stored.getInstanceOfIViewModelView(type, context)
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(type: KType): IViewModelAllSimpleListIdRelation<*>? {
        return stored.getInstanceOfIViewModelAllSimpleListIdRelation(type, context)
    }
    //endregion

    //region paging
    fun viewModelGetViewAllPaging(idRelation: Int) = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewAllPaging(idRelation).asPagingSourceFactory()
        ).flow
    }

    override fun viewModelViewAllSimpleList(idRelation: Int): List<TEntity>? =
        dao.viewAllSimpleList(idRelation)
    //endregion
}