package com.vsg.helper.common.util.viewModel

import androidx.lifecycle.LiveData
import com.vsg.helper.common.model.IEntity

interface IViewModelAllById<TEntity> where TEntity: IEntity {
    fun viewModelViewAll(id: Long): LiveData<List<TEntity>>?
}