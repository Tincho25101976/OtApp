package com.vsg.helper.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.vsg.helper.common.adapter.DataStringAdapter
import com.vsg.helper.R

class UIStringDataAdapterWithTag<T>(context: Context, list: List<DataStringAdapter<T>>) :
    UIAdapterBase<DataStringAdapter<T>>(context, R.layout.list_item_string_with_tag, list) {
    override fun setView(view: View, item: DataStringAdapter<T>, position: Int) {
        view.findViewById<TextView>(R.id.listItemString).text = item.data
    }

    fun getPosition(value: DataStringAdapter<T>): Int = map[value] ?: -1
    fun tag(key: String): ((T) -> Boolean)? = data.firstOrNull { it.data == key }?.tag
}