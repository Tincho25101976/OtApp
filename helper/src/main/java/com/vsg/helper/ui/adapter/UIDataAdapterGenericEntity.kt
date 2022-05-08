package com.vsg.helper.ui.adapter

import android.content.Context
import android.widget.Filterable
import com.vsg.helper.common.model.IEntity

class UIDataAdapterGenericEntity<T>(context: Context, list: List<T>, ignoreCase: Boolean = true) :
    UIDataAdapterGenericBase<T>(context, list, ignoreCase), Filterable
        where T : IDataAdapterTitle, T : IEntity {
}