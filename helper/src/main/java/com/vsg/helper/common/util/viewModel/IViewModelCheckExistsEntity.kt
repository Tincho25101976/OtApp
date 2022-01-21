package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelCheckExistsEntity<TEntity : IEntity> {
    fun checkExistsEntity(entity: TEntity?): Boolean
    fun isEntity(entity: TEntity?): Boolean
}