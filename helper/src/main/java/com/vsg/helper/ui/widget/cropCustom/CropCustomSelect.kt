package com.vsg.helper.ui.widget.cropCustom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.ui.widget.shapeCustom.ShapeCustomSelect

class CropCustomSelect constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr, defStyleRest) {
    //region properties
    private lateinit var tCmdSusses: ImageView
    //endregion

    //region methods
    init {
//        <ImageView
//        android:id="@+id/DialogGenericPictureChooseDelete"
//        android:layout_width="@dimen/DialogCrudPictureChooseSize"
//        android:layout_height="@dimen/DialogCrudPictureChooseSize"
//        android:layout_alignBottom="@id/DialogGenericPicture"
//        android:layout_toStartOf="@id/DialogGenericPicture"
//        android:src="@drawable/pic_choose_delete" />
        tCmdSusses = ImageView(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutLinealLayout().apply {
                height = SIZE_CMD_SUCCESS
                width = SIZE_CMD_SUCCESS
            }
            setImageDrawable(ctx.getDrawable( R.drawable.pic_ok))
        }

        val layout = LinearLayout(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
                gravity = Gravity.CENTER
            }
            addView(tCmdSusses)
        }
    }
    //endregion

    companion object {
        const val SIZE_CMD_SUCCESS = 32
    }
}