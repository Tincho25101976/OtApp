package com.vsg.helper.ui.widget.cropCustom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import androidx.core.view.setMargins
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.ui.widget.cropImage.CropImageView

@SuppressLint("UseCompatLoadingForDrawables")
class CropCustomSelect constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr, defStyleRest) {

    //region events
    var onEventRaiseCrop: (() -> Unit)? = null
    var onEventChangeShape: ((CropImageView.CropMode) -> Unit)? = null
    //endregion

    //region properties
    private var tCmdCrop: ImageView
    private var tRadioOption: RadioGroup
    private var tRadioFit: RadioButton?
    private var tRadioSquare: RadioButton?
    private var tRadioCircle: RadioButton?
    private var tRadioFree: RadioButton?

    private val dbShapeId: MutableList<Pair<CropImageView.CropMode, Int>> = mutableListOf()
    //endregion

    //region methods
    init {
        tRadioFit = getRadioButton(CropImageView.CropMode.FIT_IMAGE, true)
        tRadioFree = getRadioButton(CropImageView.CropMode.CUSTOM)
        tRadioSquare = getRadioButton(CropImageView.CropMode.SQUARE)
        tRadioCircle = getRadioButton(CropImageView.CropMode.CIRCLE)
        tRadioOption = RadioGroup(ctx).apply {
            id = View.generateViewId()
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                gravity = Gravity.CENTER
                orientation = RadioGroup.HORIZONTAL
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            setOnCheckedChangeListener { _, id ->
                if (dbShapeId.any { it.second == id }) onEventChangeShape?.invoke(dbShapeId.first { it.second == id }.first)
            }
            addView(tRadioFit)
            addView(tRadioSquare)
            addView(tRadioCircle)
            addView(tRadioFree)
        }

        tCmdCrop = ImageView(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = SIZE_CMD_SUCCESS.toPixel()
                width = SIZE_CMD_SUCCESS.toPixel()
                setMargins(15.toPixel())
                addRule(ALIGN_PARENT_END)
                addRule(BELOW, tRadioOption.id)
            }
            setImageDrawable(ctx.getDrawable(R.drawable.pic_crop))
            setOnClickListener { onEventRaiseCrop?.invoke() }
        }

        this.apply {
            addView(tRadioOption)
            addView(tCmdCrop)
        }
    }

    private fun getCropName(type: CropImageView.CropMode): String =
        when (type) {
            CropImageView.CropMode.FIT_IMAGE -> "Fit"
            CropImageView.CropMode.CUSTOM -> "Custom"
            CropImageView.CropMode.CIRCLE -> "Circle"
            CropImageView.CropMode.SQUARE -> "Square"
            else -> ""
        }

    private fun getRadioButton(
        type: CropImageView.CropMode,
        checked: Boolean = false
    ): RadioButton? {
        if (dbShapeId.any { it.first == type }) return null
        val result = RadioButton(ctx).apply {
            id = View.generateViewId()
            isChecked = checked
            text = getCropName(type)
            setTextColor(Color.BLACK)

            layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout()
                    .apply {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        setMargins(5)
                    }
        }
        dbShapeId.add(type to result.id)
        return result
    }

    fun setTypeFace(value: Typeface){
        arrayOf(tRadioCircle, tRadioFit, tRadioFree, tRadioSquare).filterNotNull()
            .forEach { it.typeface = value }
    }
    //endregion

    companion object {
        const val SIZE_CMD_SUCCESS = 32
    }
}