package com.vsg.helper.common.model.viewModel

import android.app.Application
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.*
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.MakeGenericViewModelPagingRelation
import kotlin.reflect.KType

@ExperimentalStdlibApi
abstract class ViewModelGenericRelation<TDao, TEntity>(dao: TDao, context: Application) :
    MakeGenericViewModelPagingRelation<TDao, TEntity>(dao, context), IViewModelHasItemsRelation
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPagingRelation<TEntity>,
              TDao : IDaoAllTextSearchRelation,
              TDao : IDaoAllSimpleListRelation<TEntity>,
              TDao : IDaoHastItemRelation,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {
    //region IViewModel
    override fun getInstanceOfIViewModelView(type: KType): IViewModelView<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelView(type, context)
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(type: KType): IViewModelAllSimpleListIdRelation<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelAllSimpleListIdRelation(type, context)
    }
    //endregion

    //region hasItemWithRelation
    override fun viewModelViewHasItems(idRelation: Int): Boolean = dao.viewHasItems(idRelation)
    //endregion


}