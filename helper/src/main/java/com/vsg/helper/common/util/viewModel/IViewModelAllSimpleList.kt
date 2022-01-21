package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelAllSimpleList<TEntity> where TEntity: IEntity {
    fun viewModelViewAllSimpleList(): List<TEntity>?
}