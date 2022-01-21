package com.vsg.helper.common.util.viewModel

import com.vsg.helper.common.model.IEntity

interface IViewModelGetDefault<T: IEntity> {
    fun viewModelGetDefault(): T?
}