package com.vsg.helper.common.util.viewModel.util

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import kotlin.reflect.KType

data class DataMakeIViewModelAllSimpleListIdRelation<T>(
    val kType: KType,
    val viewModel: IViewModelAllSimpleListIdRelation<T>
) where T : IEntity
