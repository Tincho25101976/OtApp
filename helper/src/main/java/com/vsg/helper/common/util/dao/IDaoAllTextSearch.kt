package com.vsg.helper.common.util.dao

import androidx.lifecycle.LiveData

interface IDaoAllTextSearch {
    fun viewGetAllTextSearch(): LiveData<List<String>>
}