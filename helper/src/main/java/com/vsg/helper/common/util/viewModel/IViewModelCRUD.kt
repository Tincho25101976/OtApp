package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelCRUD<TEntity> where TEntity : IEntity {
    fun viewModelInsert(item: TEntity): Boolean
    fun viewModelInsert(item: List<TEntity>): Boolean
    fun viewModelUpdate(item: TEntity): Boolean
    fun viewModelDelete(item: TEntity): Boolean
}