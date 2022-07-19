package com.vsg.helper.ui.widget.shapeCustom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.Gravity.HORIZONTAL_GRAVITY_MASK
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.toEditable
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.ui.popup.viewer.picture.UICustomDialogViewerEditor
import com.vsg.helper.ui.widget.colorPicker.ColorPicker
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import ja.burhanrashid52.photoeditor.shape.ShapeType

@SuppressLint("UseCompatLoadingForDrawables")
class ShapeCustomSelect @JvmOverloads constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr, defStyleRest) {
    //region properties
    private lateinit var tColorPickerLine: ColorPicker
    private lateinit var tSeekSizeLine: SeekBar
    private lateinit var tSampleLine: TextView
    private lateinit var tRadioOption: RadioGroup
    private var tBrushDraw: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }

    private val dbShapeId: List<Pair<ShapeType, Int>> = listOf()

    var typeface: Typeface
        get() = tSampleLine.typeface
        set(value) {
            tSampleLine.typeface = value
        }
    //endregion

    //region methods
    init {
        tRadioOption = RadioGroup(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                gravity = CENTER
                orientation = RadioGroup.HORIZONTAL
            }
            addView()
        }

        tColorPickerLine = ColorPicker(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply { height = 65.toPixel() }
            onEventChangeColorPicker = {
                tBrushDraw.color = it
                tSampleLine.setBackgroundColor(it)
            }
        }
        tSeekSizeLine = SeekBar(ctx).apply {
            max =
                UICustomDialogViewerEditor.MAXIMO_SIZE_DRAW * UICustomDialogViewerEditor.FACTOR_SIZE_DRAW
            min =
                UICustomDialogViewerEditor.MINIMO_SIZE_DRAW * UICustomDialogViewerEditor.FACTOR_SIZE_DRAW
            progress =
                (UICustomDialogViewerEditor.DEFAULT_SIZE_DRAW * UICustomDialogViewerEditor.FACTOR_SIZE_DRAW).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    tBrushDraw.strokeWidth = when (seekBar != null) {
                        true -> seekBar.progress.toFloat() / UICustomDialogViewerEditor.FACTOR_SIZE_DRAW
                        false -> UICustomDialogViewerEditor.DEFAULT_SIZE_DRAW
                    }
                    tSampleLine.text =
                        tBrushDraw.strokeWidth.toFormat(UICustomDialogViewerEditor.DECIMAL_SHOW_SIZE_DRAW)
                            .toEditable()
                }
            })
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        tSampleLine = TextView(ctx).apply {
            text = null
            setBackgroundColor(Color.WHITE)
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = 32.toPixel()
                width = 32.toPixel()
            }
            background = ctx.getDrawable(R.drawable.border_view)
            gravity = CENTER
            textSize = 7.toPixel().toFloat()
            text =
                tBrushDraw.strokeWidth.toFormat(UICustomDialogViewerEditor.DECIMAL_SHOW_SIZE_DRAW)
                    .toEditable()
            setBackgroundColor(tColorPickerLine.color)

        }
        val add = LinearLayout(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = 36.toPixel()
                gravity = CENTER
            }
            orientation = LinearLayout.HORIZONTAL
            addView(tSampleLine)
            addView(tSeekSizeLine)
        }
        this.apply {
            addView(tColorPickerLine)
            addView(add)
        }
        //endregion
    }
    private fun getRadioButton(type:ShapeType, checked: Boolean = false):RadioButton{
        val result = RadioButton(ctx).apply {
            id = View.generateViewId()
            isChecked = checked
            setText(type)
            if(dbShapeId.any { it.first })
        }
        return result
    }
}