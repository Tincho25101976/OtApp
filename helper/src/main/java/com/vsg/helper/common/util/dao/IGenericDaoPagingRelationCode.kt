package com.vsg.helper.common.util.dao

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled

interface IGenericDaoPagingRelationCode<T> :
    IGenericDaoPagingRelation<T>,
//    IDaoNextCode,
    IDaoAllTextSearchRelation,
    IDaoAllSimpleListRelation<T>,
    IDaoHastItemRelation
        where T : IResultRecyclerAdapter,
              T : IEntityPagingLayoutPosition,
              T : IEntity,
              T : IIsEnabled,
              T : Comparable<T>