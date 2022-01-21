package com.vsg.helper.helper.progress

import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.ui.adapter.UIIDataAdapter

interface ICallbackProcessGetAdapter<T> : ICallbackProgress where T : IDataAdapter {
    fun processGetAdapter(): UIIDataAdapter<T>
}