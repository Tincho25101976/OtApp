package com.vsg.helper.common.util.addItem

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.model.ItemBasePaging

abstract class ItemBasePagingAddItem<T> : ItemBasePaging<T>(), IAddItemEntity
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity, T : IReference,
              T : IEntityPagingLayoutPosition