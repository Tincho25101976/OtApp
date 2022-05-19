package com.vsg.ot.common.model.init.dao

import android.text.Spanned
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityCreateDate
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IDaoAllTextSearch
import com.vsg.helper.common.util.dao.IGenericDaoPagingParse
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.ui.data.log.CustomLog
import kotlinx.coroutines.runBlocking

abstract class DaoGenericOtParse<T> : IGenericDaoPagingParse<T>, IDaoAllTextSearch
        where T : IResultRecyclerAdapter,
              T : IEntityPagingLayoutPosition,
              T : IEntity,
              T : IIsEnabled,
              T : IEntityCreateDate,
              T : Comparable<T> {

    //region events
    override var onProgress: ((Int, Int, Double) -> Unit)? = null
    //endregion

    //region properties
    private val log: CustomLog = CustomLog()
    //endregion

    //region methods
    override fun processWithProgress(data: List<T>): Boolean = insert(data)

    override fun insert(item: List<T>): Boolean {
        if (!item.any()) return false
        var i = 0
        var c = 0
        val count = item.count()
        item.forEach {
            try {
                runBlocking {
                    c += (insert(it) > 0L) then 1 or 0
                }
                onResultProgreso(++i, count)
            } catch (ex: Exception) {

            }
        }
        return c == count
    }

    abstract override fun deleteAll()
    override fun readToList(): MutableList<T> {
        val result = viewAllSimpleList()
        return (result == null) then mutableListOf<T>() or result!!.toMutableList()
    }

    override fun getLog(): Spanned = log.getAndNew()
    //endregion

    //region callback
    private fun onResultProgreso(actual: Int, total: Int) {
        val porcentaje: Double = (actual * 100 / total).toDouble()
        onProgress?.invoke(actual, total, porcentaje)
    }
    //endregion
}