package com.vsg.helper.common.util.viewModel.viewRoom

import com.vsg.helper.common.model.viewRoom.IEntityViewRoom

interface IViewModelAllSimpleListViewRoom<TEntity> where TEntity : IEntityViewRoom<TEntity> {
    fun viewModelViewAllSimpleList(idRelation: Long): List<TEntity>
}