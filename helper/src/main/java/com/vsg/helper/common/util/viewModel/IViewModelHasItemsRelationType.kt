package com.vsg.helper.common.util.viewModel

interface IViewModelHasItemsRelationType<TFilter> where TFilter : Enum<TFilter> {
    fun viewModelViewHasItems(idRelation: Long, filter: TFilter): Boolean
}