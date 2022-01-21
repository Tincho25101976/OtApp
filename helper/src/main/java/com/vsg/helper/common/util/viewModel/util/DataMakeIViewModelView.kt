package com.vsg.helper.common.util.viewModel.util

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.viewModel.IViewModelView
import kotlin.reflect.KType

data class DataMakeIViewModelView<T>(
    val type: KType,
    val viewModel: IViewModelView<T>
) where T : IEntity