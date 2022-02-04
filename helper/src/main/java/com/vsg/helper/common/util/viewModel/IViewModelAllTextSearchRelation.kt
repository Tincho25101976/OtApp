package com.vsg.helper.common.util.viewModel

import androidx.lifecycle.LiveData

interface IViewModelAllTextSearchRelation {
    fun viewModelGetAllTextSearch(idRelation: Int): LiveData<List<String>>
}