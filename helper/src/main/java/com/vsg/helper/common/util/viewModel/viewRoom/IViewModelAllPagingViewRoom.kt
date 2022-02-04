package com.vsg.helper.common.util.viewModel.viewRoom

import androidx.paging.PagingData
import com.vsg.helper.common.model.viewRoom.IEntityViewRoom
import kotlinx.coroutines.flow.Flow

interface IViewModelAllPagingViewRoom<TEntity>
        where TEntity : IEntityViewRoom<TEntity> {
    fun viewModelGetViewAllPaging(idRelation: Int): Flow<PagingData<TEntity>>
}