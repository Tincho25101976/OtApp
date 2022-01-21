package com.vsg.helper.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.R

class UIIDataAdapter<T>(context: Context, list: List<T>): UIAdapterBase<T>(context, R.layout.list_item_idata_adapter, list) where T: IDataAdapter {
    override fun setView(view: View, item: T, position: Int) {
        view.findViewById<TextView>(R.id.tv_list_item_adapter_title).text = item.titleAdapter()
        view.findViewById<TextView>(R.id.tv_list_item_adapter_data).text = item.bodyAdapter()
    }
}