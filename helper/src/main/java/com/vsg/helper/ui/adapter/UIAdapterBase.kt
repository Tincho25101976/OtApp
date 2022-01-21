package com.vsg.helper.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.LayoutRes
import com.vsg.helper.helper.font.FontManager

abstract class UIAdapterBase<T>(
    var context: Context,
    @LayoutRes var layout: Int,
    var data: List<T>
) :
    BaseAdapter() where T : Any {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    protected var map: Map<T, Int>

    init {
        var index: Int = 0
        map = data.map { it to index++ }.toMap()
    }

    override fun getCount(): Int = data.size
    override fun getItem(position: Int): Any? = data[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(layout, parent, false)
        FontManager(context).replaceFonts(rowView as ViewGroup)
        setView(rowView, data[position], position)
        return rowView
    }

    protected abstract fun setView(view: View, item: T, position: Int)


}