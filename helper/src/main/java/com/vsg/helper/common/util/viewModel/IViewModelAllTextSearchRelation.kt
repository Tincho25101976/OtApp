package com.vsg.helper.common.util.viewModel

import androidx.lifecycle.LiveData

interface IViewModelAllTextSearchRelation {
    fun viewModelGetAllTextSearch(idRelation: Long): LiveData<List<String>>
}