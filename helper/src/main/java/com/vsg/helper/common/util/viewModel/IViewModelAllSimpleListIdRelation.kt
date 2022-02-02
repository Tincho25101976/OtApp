package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelAllSimpleListIdRelation<TEntity> where TEntity: IEntity {
    fun viewModelViewAllSimpleList(idRelation: Int): List<TEntity>?
}