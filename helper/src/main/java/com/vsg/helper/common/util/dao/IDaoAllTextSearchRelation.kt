package com.vsg.helper.common.util.dao

import androidx.lifecycle.LiveData

interface IDaoAllTextSearchRelation {
    fun viewGetAllTextSearch(idRelation: Int): LiveData<List<String>>
}