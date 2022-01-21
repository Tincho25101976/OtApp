package com.vsg.helper.common.util.addItem

import android.app.Application
import androidx.lifecycle.LiveData
import com.vsg.helper.common.util.viewModel.IViewModelAllById
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import kotlinx.coroutines.runBlocking

abstract class MakeGenericAddItemViewModel<TDao, TEntity>(
    dao: TDao,
    context: Application
) :
    MakeGenericViewModel<TDao, TEntity>(
        dao,
        context
    ), IViewModelAllById<TEntity>
        where TDao : IAddItemDao<TEntity>,
              TEntity : ItemBaseAddItem {
    override fun viewModelViewAll(id: Long): LiveData<List<TEntity>>? =
        runBlocking { return@runBlocking dao.viewAll(id) }

    fun viewModelAny(id: Long) = runBlocking { return@runBlocking dao.any(id) }
    fun viewModelViewDefault(id: Long): LiveData<List<TEntity>> =
        runBlocking { return@runBlocking dao.viewDefault(id) }
}