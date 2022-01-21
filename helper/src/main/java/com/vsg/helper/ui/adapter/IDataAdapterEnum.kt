package com.vsg.helper.ui.adapter

import com.vsg.helper.ui.adapter.paging.IDataAdapterValue

interface IDataAdapterEnum: IDataAdapterValue {
    val show: Boolean
    val order: Int
    val default : Boolean
    val isException: Boolean
}