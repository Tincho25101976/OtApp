package com.vsg.helper.ui.widget.spinner

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.ui.adapter.IDataAdapterTitle

data class AdapterTitleTest(override val title: String, override var id: Int) : IDataAdapterTitle,
    IEntity