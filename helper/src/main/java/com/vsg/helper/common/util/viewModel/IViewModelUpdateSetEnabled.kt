package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelUpdateSetEnabled<T> where T: IEntity {
    fun viewModelUpdateSetEnabled(item: T)
}