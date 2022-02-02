package com.vsg.helper.common.model.viewModel

import android.app.Application
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IDaoCheckExitsEntity
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPaging
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelUpdateSetEnabled
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.MakeGenericViewModelPaging
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KType

@ExperimentalStdlibApi
abstract class ViewModelGeneric<TDao, TEntity>(dao: TDao, application: Application) :
    MakeGenericViewModelPaging<TDao, TEntity>(
        dao,
        application
    ), IViewModelUpdateSetEnabled<TEntity>
        where TDao : IGenericDao<TEntity>,
              TDao : IDaoCheckExitsEntity,
              TDao : IGenericDaoPaging<TEntity>,
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

    //region enabled
    override fun viewModelUpdateSetEnabled(item: TEntity) = runBlocking {
        dao.updateSetEnabled(item.id)
    }
    //endregion

//    //region paging
//    val viewModelGetViewAllPaging = runBlocking {
//        return@runBlocking Pager<Int, TEntity>(
//            pagingConfig,
//            0,
//            dao.viewAllPaging().asPagingSourceFactory()
//        ).flow
//    }
//    //endregion
}