package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelGetDefaultRelation<T : IEntity> {
    fun viewModelGetDefault(idRelation: Int): T?
}