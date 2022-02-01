package com.vsg.helper.common.util.dao.viewRoom

import androidx.paging.DataSource
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import com.vsg.helper.common.util.dao.IDaoAllTextSearchRelation

interface IGenericDaoPagingRelationViewRoom<TEntity, TViewRoom>: IDaoAllTextSearchRelation
        where TEntity : IEntityViewRoom<TEntity>,
              TViewRoom : IEntity {
    fun viewAllPagingViewRoom(idRelation: Int): DataSource.Factory<Int, TViewRoom>
    fun viewAllListViewRoom(idRelation: Int): List<TViewRoom>?
}