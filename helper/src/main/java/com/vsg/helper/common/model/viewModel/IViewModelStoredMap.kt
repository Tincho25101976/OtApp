package com.vsg.helper.common.model.viewModel

import android.app.Application
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import kotlin.reflect.KType

interface IViewModelStoredMap {
    fun getInstanceOfIViewModelView(type: KType, context: Application): IViewModelView<*>?
    fun getInstanceOfIViewModelAllSimpleListIdRelation(
        type: KType,
        context: Application
    ): IViewModelAllSimpleListIdRelation<*>?
}