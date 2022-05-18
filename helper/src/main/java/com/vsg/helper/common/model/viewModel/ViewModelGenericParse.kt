package com.vsg.helper.common.model.viewModel

import android.app.Application
import android.text.Spanned
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IEntityParse
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IDaoCheckExitsEntity
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPaging
import com.vsg.helper.common.util.dao.IGenericDaoPagingParse
import com.vsg.helper.common.util.viewModel.IViewModelAllPagingParse

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
    ), IViewModelAllPagingParse<TEntity>
        where TDao : IGenericDao<TEntity>,
              TDao : IDaoCheckExitsEntity,
              TDao : IGenericDaoPaging<TEntity>,
              TDao : IGenericDaoPagingParse<TEntity>,
              TEntity : IEntity,
              TEntity : IEntityParse<TEntity>,
              TEntity : IIsEnabled,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {

    override fun processWithProgress(data: List<TEntity>): Boolean {
        return insert(data)
    }

    override var onProgress: ((Int, Int, Double) -> Unit)? = null

    override fun viewModelViewAllSimpleList(): List<TEntity> =
        dao.viewAllSimpleList() ?: listOf()

    override fun deleteAll() = dao.deleteAll()
    override fun insert(item: List<TEntity>): Boolean {
        dao.onProgress = { current, total, percentage ->
            onProgress?.invoke(current, total, percentage)
        }
        return dao.insert(item)
    }

    override fun getLog(): Spanned = dao.getLog()
    override fun readToList(): MutableList<TEntity> = dao.readToList()
}