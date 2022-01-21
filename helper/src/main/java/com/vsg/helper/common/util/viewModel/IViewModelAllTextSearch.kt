package com.vsg.helper.common.util.viewModel

import androidx.lifecycle.LiveData

interface IViewModelAllTextSearch {
    fun viewModelGetAllTextSearch(): LiveData<List<String>>
}