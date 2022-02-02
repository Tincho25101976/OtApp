package com.vsg.helper.common.util.viewModel

interface IViewModelHasItemsRelationType<TFilter> where TFilter : Enum<TFilter> {
    fun viewModelViewHasItems(idRelation: Int, filter: TFilter): Boolean
}