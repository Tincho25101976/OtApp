package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsDefault

interface IViewModelUpdateIsDefault<TEntity>
        where TEntity : IEntity,
              TEntity : IIsDefault {
    fun viewModelSetIsDefault(item: TEntity)
}