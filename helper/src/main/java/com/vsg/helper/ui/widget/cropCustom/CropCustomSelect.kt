package com.vsg.helper.ui.widget.cropCustom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.setMargins
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel

@SuppressLint("UseCompatLoadingForDrawables")
class CropCustomSelect constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr, defStyleRest) {
    //region events
    var onEventRaiseCrop: (() -> Unit)? = null
    //endregion

    //region properties
    private var tCmdCrop: ImageView
    //endregion

    //region methods
    init {
        tCmdCrop = ImageView(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = SIZE_CMD_SUCCESS.toPixel()
                width = SIZE_CMD_SUCCESS.toPixel()
                setMargins(15.toPixel())
//                addRule(RelativeLayout.ALIGN_PARENT_END)
//                addRule(RelativeLayout.ALIGN_PARENT_TOP)
            }
            setImageDrawable(ctx.getDrawable(R.drawable.pic_ok))
            setOnClickListener { onEventRaiseCrop?.invoke() }
        }

//        val layout = LinearLayout(ctx).apply {
//            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
//                width = ViewGroup.LayoutParams.MATCH_PARENT
//                height = ViewGroup.LayoutParams.MATCH_PARENT
//            }
//            addView(tCmdCrop)
//        }
        this.apply {
            addView(tCmdCrop)
        }
    }
    //endregion

    companion object {
        const val SIZE_CMD_SUCCESS = 48
    }
}