package com.vsg.helper.ui.adapter.paging

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vsg.helper.R
import com.vsg.helper.common.adapter.IRecyclerAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.helper.font.FontManager
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel

class UIRecyclerAdapterPagingData<TEntity> :
    PagingDataAdapter<TEntity, UIRecyclerAdapterPagingData<TEntity>.UIRecyclerViewHolder>(Diff<TEntity>().diffCallback)
        where TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : IEntity,
              TEntity : Comparable<TEntity> {

    //region handler
    var onEventClickItem: ((View, Int, Int) -> Unit)? = null
    var onEventClickLongItem: ((View, Long) -> Unit)? = null
    var onEventSetTextViewTitle: ((TextView) -> Unit)? = null
    var onEventGetViewForMenu: ((TEntity) -> Unit)? = null
    var onEventSetLayoutForMenu: (() -> Int?)? = null
    var onEventGetViewGroupForMenu: ((View) -> Unit)? = null
    var onEventGetItemAfterBind: ((TEntity) -> TEntity)? = null
    //endregion

    //region viewHolderReference
    private var currentItem: TEntity? = null
    private lateinit var viewMenu: View
    private var isAdapterWithMenu: Boolean = false
    private var isFirst: Boolean = true

    @LayoutRes
    private var layoutMenu: Int = 0
    private lateinit var parentViewGroup: ViewGroup
    //endregion

    override fun onBindViewHolder(holder: UIRecyclerViewHolder, position: Int) {
        val item: TEntity = getItem(position) ?: return
        if (!isAdapterWithMenu) {
            val modification = onEventGetItemAfterBind?.invoke(item) ?: item
            holder.bindTo(modification)
            return
        }
        item.layoutPosition = position
        if (currentItem == null) {
            val modification = onEventGetItemAfterBind?.invoke(item) ?: item
            holder.bindTo(modification)
            return
        }
        if (currentItem!!.layoutPosition == position && isAdapterWithMenu) {
            onEventGetViewForMenu?.invoke(currentItem!!)
            currentItem = null
        } else {
            val modification = onEventGetItemAfterBind?.invoke(item) ?: item
            holder.bindTo(modification)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UIRecyclerViewHolder {
        this.parentViewGroup = parent
        if (isFirst) {
            makeViewForMenu(parent)
            isFirst = false
        }
        return if (viewType == StatusMenu.SHOW.value) {
            viewMenu = makeViewForMenu(parent)
            UIRecyclerViewHolder(viewMenu)
        } else {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_recycler_view_idata_adapter, parent, false)
            FontManager(parent.context).replaceFonts(v as ViewGroup)
            UIRecyclerViewHolder(v)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var status: StatusMenu = StatusMenu.HIDE
        if (!isAdapterWithMenu) status = StatusMenu.HIDE
        if (currentItem == null) status = StatusMenu.HIDE
        if (currentItem?.layoutPosition == position) status = StatusMenu.SHOW
        return status.value
    }

    //region viewHolder
    inner class UIRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item: TEntity? = null
        fun bindTo(data: TEntity?) {
            if (data == null) return
            this.item = data
            custom(data.getRecyclerAdapter())
        }

        private fun custom(data: IRecyclerAdapter) = with(itemView)
        {
            val pic = findViewById<ImageView>(R.id.listItemRecyclerViewDataPicture)
            pic.setImageBitmap(null)
            if (data.isBitmap) pic.setImageBitmap(data.bitmap)
            else if (data.picture > 0) pic.setImageResource(data.picture)
            data.sizePictureHeight = resources.getInteger(R.integer.CustomSizePictureHeight)
            data.sizePictureWidth = resources.getInteger(R.integer.CustomSizePictureWidth)
            val layoutParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    data.sizePictureWidth.toPixel(),
                    data.sizePictureHeight.toPixel()
                )

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

            setOnClickListener {
                onEventClickItem?.invoke(itemView, itemView.tag as Int, item?.layoutPosition ?: 0)
            }
            setOnLongClickListener {
                onEventClickLongItem?.invoke(itemView, itemView.tag as Long)
                true
            }
        }
    }
    //endregion

    //region menu
    fun showMenu(item: Int) {
        if (!isAdapterWithMenu) return
        this.currentItem = getItem(item)?.apply { layoutPosition = item }
    }

    private fun makeViewForMenu(parent: ViewGroup): View {
        var viewMenu: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_recycler_view_idata_adapter, parent, false)
        try {
            val layout = onEventSetLayoutForMenu?.invoke() ?: 0
            if (layout > 0) {
                layoutMenu = layout
                viewMenu = LayoutInflater.from(parent.context)
                    .inflate(
                        layoutMenu,
                        parent,
                        false
                    )
                FontManager(parent.context).replaceFonts(viewMenu as ViewGroup)
                onEventGetViewGroupForMenu?.invoke(viewMenu)
                isAdapterWithMenu = true
            }
        } catch (e: Exception) {
            isAdapterWithMenu = false
        }
        return viewMenu
    }
    //endregion

    companion object {
        class Diff<T> where T : IEntity, T : Comparable<T> {
            val diffCallback = object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
                    oldItem.id == newItem.id

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                    oldItem == newItem
            }
        }
    }
}