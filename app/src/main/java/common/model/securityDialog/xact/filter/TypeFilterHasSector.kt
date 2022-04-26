package com.vsg.ot.common.model.securityDialog.xact.filter

import com.vsg.helper.ui.adapter.paging.IDataAdapterValue

enum class TypeFilterHasSector(override val value: Int, override val title: String) :
    IDataAdapterValue {
    SECTOR(value = 1, title = "Sector")
}