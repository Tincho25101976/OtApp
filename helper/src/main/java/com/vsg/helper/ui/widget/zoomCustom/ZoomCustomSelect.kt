package com.vsg.helper.ui.widget.zoomCustom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.setMargins
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel

@SuppressLint("UseCompatLoadingForDrawables")
class ZoomCustomSelect constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr, defStyleRest) {

    //region events
    var onEventRaiseZoom: (() -> Unit)? = null
    //endregion

    //region properties
    private var tCmdZoom: ImageView = ImageView(ctx).apply {
        layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
            height = SIZE_CMD_SUCCESS.toPixel()
            width = SIZE_CMD_SUCCESS.toPixel()
            setMargins(15.toPixel())
            addRule(ALIGN_PARENT_END)
        }
        setImageDrawable(ctx.getDrawable(R.drawable.pic_zoom))
        setOnClickListener { onEventRaiseZoom?.invoke() }
    }
    //endregion

    //region methods
    init {
        this.apply {
            addView(tCmdZoom)
        }
    }
    //endregion

    companion object {
        const val SIZE_CMD_SUCCESS = 32
    }
}