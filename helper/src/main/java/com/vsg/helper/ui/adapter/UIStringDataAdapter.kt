package com.vsg.helper.ui.adapter

import android.app.Activity
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.vsg.helper.R

class UIStringDataAdapter(context: Activity, list: List<String>, val ignoreCase: Boolean = true) :
    UIAdapterBase<String>(context, R.layout.list_item_string_with_tag, list), Filterable {

    var onEventSetTextView: ((TextView) -> Unit)? = null
    private var temp: List<String> = list

    override fun getItem(position: Int): String? {
        return when (position > temp.lastIndex) {
            true -> null
            false -> temp[position]
        }
    }

    override fun getCount(): Int = temp.size
    override fun setView(view: View, item: String, position: Int) {
        //view.findViewById<TextView>(R.id.listItemString)
        view.findViewById<TextView>(R.id.listItemString).apply {
            text = getItem(position)
            onEventSetTextView?.invoke(this)
        }
    }

    fun getPosition(value: String): Int = map[value] ?: -1

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                temp = filterResults.values as List<String>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()
                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty()) data
                else data.filter {
                    it.contains(queryString, ignoreCase)
                }
                return filterResults
            }
        }
    }
}