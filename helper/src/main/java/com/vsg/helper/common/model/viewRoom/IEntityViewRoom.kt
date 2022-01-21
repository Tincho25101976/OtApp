package com.vsg.helper.common.model.viewRoom

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsEnabled

interface IEntityViewRoom<TEntity> :
    IEntity,
    IIsEnabled,
    Comparable<TEntity>,
    IEntityPagingLayoutPosition,
    IResultRecyclerAdapter{
}