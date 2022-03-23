package com.vsg.helper.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.vsg.helper.common.adapter.IRecyclerAdapter
import com.vsg.helper.helper.font.FontManager
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.R

class UIRecyclerAdapter<T>(private val list: List<T>) :
    RecyclerView.Adapter<UIRecyclerAdapter<T>.ViewHolder>() where T : IRecyclerAdapter {

    //region handler
    var onEventClickItem: ((View, Int) -> Unit)? = null
    var onEventClickLongItem: ((View, Int) -> Unit)? = null
    var onEventSetTextViewTitle: ((TextView) -> Unit)? = null
    //endregion

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun custom(data: T) = with(itemView) {
            val pic = findViewById<ImageView>(R.id.listItemRecyclerViewDataPicture)
            pic.setImageBitmap(null)
            if(data.isBitmap) pic.setImageBitmap(data.bitmap)
            else if (data.picture > 0)  pic.setImageResource(data.picture)
            val layoutParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(data.sizePictureWidth.toPixel(),
                                          data.sizePictureHeight.toPixel())
            pic.layoutParams = layoutParams
            pic.scaleType = ImageView.ScaleType.FIT_END


            findViewById<TextView>(R.id.listItemRecyclerViewDataTitle).apply {
                text = data.title
                textSize = data.textSizeTitle.toFloat()
                setTextColor(Color.BLACK)
                onEventSetTextViewTitle?.invoke(this)
            }
            findViewById<TextView>(R.id.listItemRecyclerViewDataBody).apply {
                text = data.body
                textSize = data.textSizeBody.toFloat()
                setTextColor(Color.BLACK)
            }

            val rating: RatingBar = findViewById(R.id.listItemRecyclerViewDataRating)
            if (data.rating < 0) rating.isVisible = false
            else rating.rating = data.rating * (5 / 100)
            tag = data.id

            setOnClickListener { onEventClickItem?.invoke(itemView, itemView.tag as Int) }
            setOnLongClickListener {
                onEventClickLongItem?.invoke(itemView, itemView.tag as Int)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recycler_view_idata_adapter, parent, false)
        FontManager(parent.context).replaceFonts(v as ViewGroup)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, posicion: Int) {
        holder.custom(list[posicion])
    }
    override fun getItemCount() = list.count()
}