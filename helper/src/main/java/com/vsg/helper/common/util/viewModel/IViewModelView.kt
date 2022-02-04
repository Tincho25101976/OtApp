package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelView<TEntity> where TEntity : IEntity {
    fun viewModelView(id: Int): TEntity?
}