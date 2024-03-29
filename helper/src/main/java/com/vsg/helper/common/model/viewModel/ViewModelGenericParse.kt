package com.vsg.helper.common.model.viewModel

import android.app.Application
import android.text.Spanned
import androidx.lifecycle.LiveData
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IEntityParse
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.*
import com.vsg.helper.common.util.viewModel.IViewModelAllPagingParse
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
abstract class ViewModelGenericParse<TDao, TEntity>(
    dao: TDao,
    application: Application,
    stored: IViewModelStoredMap
) :
    ViewModelGeneric<TDao, TEntity>(
        dao,
        application,
        stored
    ),
    IViewModelAllPagingParse<TEntity>,
    IViewModelAllTextSearch
        where TDao : IGenericDao<TEntity>,
              TDao : IDaoCheckExitsEntity,
              TDao : IGenericDaoPaging<TEntity>,
              TDao : IGenericDaoPagingParse<TEntity>,
              TDao : IDaoAllTextSearch,
              TEntity : IEntity,
              TEntity : IEntityParse<TEntity>,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {

    //region events
    override var onProgress: ((Int, Int, Double) -> Unit)? = null
    //endregion

    //region methods
    override fun processWithProgress(data: List<TEntity>): Boolean {
        return insert(data)
    }

    override fun deleteAll() = dao.deleteAll()
    override fun resetIndexIdentity() = dao.resetIndexIdentity()
    override fun insert(item: List<TEntity>): Boolean {
        if (dao.onProgress == null) {
            dao.onProgress = { current, total, percentage ->
                onProgress?.invoke(current, total, percentage)
            }
        }
        return dao.insert(item)
    }

    override fun getLog(): Spanned = dao.getLog()
    override fun readToList(): MutableList<TEntity> = dao.readToList()

    override fun viewModelViewAllSimpleList(): List<TEntity> =
        dao.viewAllSimpleList() ?: listOf()

    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewGetAllTextSearch()
    }
    //endregion
}