package com.vsg.helper.common.util.viewModel

import androidx.lifecycle.LiveData
import com.vsg.helper.common.model.IEntity

interface IViewModelAllSimpleListWithRelation<TEntity> where TEntity : IEntity {
    fun viewModelViewListWithRelations(): LiveData<List<TEntity>>?
    fun viewModelViewWithRelations(id: Int): TEntity?
}