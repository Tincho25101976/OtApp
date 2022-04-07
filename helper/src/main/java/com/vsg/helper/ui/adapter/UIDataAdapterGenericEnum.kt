package com.vsg.helper.ui.adapter

import android.content.Context
import com.vsg.helper.helper.HelperEnum.Companion.getList

class UIDataAdapterGenericEnum<T>(
    context: Context,
    val type: Class<T>,
    ignoreCase: Boolean = true
) :
    UIDataAdapterGenericBase<T>(context, getList<T>(type), ignoreCase)
        where T : IDataAdapterEnum, T : Enum<T>