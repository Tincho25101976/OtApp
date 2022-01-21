package com.vsg.helper.ui.adapter

import android.content.Context
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.vsg.helper.R

abstract class UIDataAdapterGenericBase<T>(
    context: Context,
    list: List<T>,
    val ignoreCase: Boolean = true
) :
    UIAdapterBase<T>(context, R.layout.list_item_string_with_tag, list), Filterable
        where T : IDataAdapterTitle {
    var onSetTextView: ((TextView) -> Unit)? = null
    private var temp: List<T> = list
    val dataSource: List<T>
        get() = temp

    override fun getItem(position: Int): T? {
        return when (position > temp.lastIndex) {
            true -> null
            false -> temp[position]
        }
    }

    override fun getCount(): Int = temp.size
    override fun setView(view: View, item: T, position: Int) {
        view.findViewById<TextView>(R.id.listItemString).apply {
            text = getItem(position)?.title
            onSetTextView?.invoke(this)
        }
    }

    fun getPosition(value: T): Int = map[value] ?: -1

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                temp = filterResults.values as List<T>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()
                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) data
                else data.filter {
                    it.title.contains(queryString, ignoreCase)
                }
                return filterResults
            }
        }
    }
}