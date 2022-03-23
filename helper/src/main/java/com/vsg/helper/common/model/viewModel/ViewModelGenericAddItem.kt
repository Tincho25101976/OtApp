package com.vsg.helper.common.model.viewModel

import android.app.Application
import com.vsg.helper.common.util.addItem.IAddItemDao
import com.vsg.helper.common.util.addItem.ItemBaseAddItem
import com.vsg.helper.common.util.addItem.MakeGenericAddItemViewModel
import com.vsg.helper.common.util.dao.IDaoAllUpdateIsDefault
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelUpdateIsDefault
import com.vsg.helper.common.util.viewModel.IViewModelView
import kotlin.reflect.KType

@ExperimentalStdlibApi
abstract class ViewModelGenericAddItem<TDao, TEntity>(dao: TDao, context: Application, stored: IViewModelStoredMap) :
    MakeGenericAddItemViewModel<TDao, TEntity>(dao, context, stored),
    IViewModelUpdateIsDefault<TEntity>
        where TDao : IAddItemDao<TEntity>,
              TDao : IDaoAllUpdateIsDefault,
              TEntity : ItemBaseAddItem {
    //region IViewModel
    override fun getInstanceOfIViewModelView(type: KType): IViewModelView<*>? {
        return stored.getInstanceOfIViewModelView(type, context)
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(type: KType): IViewModelAllSimpleListIdRelation<*>? {
        return stored.getInstanceOfIViewModelAllSimpleListIdRelation(type, context)
    }
    //endregion
}