package common.model.init.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IDaoAllTextSearchRelation
import com.vsg.helper.common.util.dao.IDaoHastItemRelation
import com.vsg.helper.common.util.dao.IGenericDaoPagingRelation

abstract class DaoGenericOt<T> : IGenericDaoPagingRelation<T>,
    IDaoHastItemRelation,
    IDaoAllTextSearchRelation

        where T : IResultRecyclerAdapter,
              T : IEntityPagingLayoutPosition,
              T : IEntity,
              T : IIsEnabled,
              T : Comparable<T> {

    abstract override fun viewAllPaging(): DataSource.Factory<Int, T>

    abstract override fun viewAllPaging(idRelation: Int): DataSource.Factory<Int, T>

    abstract override fun view(id: Int): T?

    abstract override fun viewAll(): LiveData<List<T>>?

    abstract override fun updateSetEnabled(data: Int)

    abstract override fun viewGetAllTextSearch(idRelation: Int): LiveData<List<String>>

    abstract override fun viewHasItems(idRelation: Int): Boolean

    abstract fun viewAllSimpleList(idRelation: Int): List<T>?
    abstract fun viewAllSimpleList(): List<T>?

    abstract override fun checkExitsEntity(entity: Int): Boolean
}