package com.vsg.helper.common.util.viewModel

import android.app.Application
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityCode
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IDaoAllTextSearchRelation
import com.vsg.helper.common.util.dao.IDaoNextCode
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPagingRelationCode

abstract class MakeGenericViewModelPagingRelationCode<TDao, TEntity>(
    dao: TDao,
    context: Application
) : MakeGenericViewModelPagingRelation<TDao, TEntity>(dao, context),
    IViewModelUpdateSetEnabled<TEntity>,
    IViewModelNextCode,
    IViewModelHasItemsRelation
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPagingRelationCode<TEntity>,
              TDao : IDaoNextCode,
              TDao : IDaoAllTextSearchRelation,
              TEntity : IEntity,
              TEntity : IEntityCode,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {

    protected abstract fun viewModelEncode(item: TEntity): TEntity?

    //region nextCode
    override fun viewModelNextAutoCode(idRelation: Int): Int = dao.viewNextAutoCode(idRelation)
    //endregion

    //region has
    override fun viewModelViewHasItems(idRelation: Int): Boolean = dao.viewHasItems(idRelation)
    //endregion
}