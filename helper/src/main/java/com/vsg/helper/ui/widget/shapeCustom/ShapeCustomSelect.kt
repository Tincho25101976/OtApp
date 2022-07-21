package com.vsg.helper.ui.widget.shapeCustom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.toEditable
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
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

    //region events
    var onEventChangeColor: ((Int) -> Unit)? = null
    var onEventChangeSize: ((Float) -> Unit)? = null
    var onEventChangeShape: ((ShapeType) -> Unit)? = null
    //endregion

    //region properties
    private var tColorPickerLine: ColorPicker
    private var tSeekSizeLine: SeekBar
    private lateinit var tSampleLine: TextView
    private var tRadioOption: RadioGroup
    private var tRadioBrush: RadioButton?
    private var tRadioOval: RadioButton?
    private var tRadioLine: RadioButton?
    private var tRadioRectangle: RadioButton?
    private var tBrushDraw: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }

    private val dbShapeId: MutableList<Pair<ShapeType, Int>> = mutableListOf()

    var typeface: Typeface
        get() = tSampleLine.typeface
        set(value) {
            tSampleLine.typeface = value
            arrayOf(tRadioLine, tRadioBrush, tRadioOval, tRadioRectangle).filterNotNull()
                .forEach { it.typeface = value }
        }
    //endregion

    //region methods
    init {
        tRadioBrush = getRadioButton(ShapeType.BRUSH, true)
        tRadioOval = getRadioButton(ShapeType.OVAL)
        tRadioRectangle = getRadioButton(ShapeType.RECTANGLE)
        tRadioLine = getRadioButton(ShapeType.LINE)
        tRadioOption = RadioGroup(ctx).apply {
            id = View.generateViewId()
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                gravity = CENTER
                orientation = RadioGroup.HORIZONTAL
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = DEFAULT_SIZE_SHAPE.toPixel()
            }
            setOnCheckedChangeListener { _, id ->
                if (dbShapeId.any { it.second == id }) onEventChangeShape?.invoke(dbShapeId.first { it.second == id }.first)
            }
            addView(tRadioBrush)
            addView(tRadioLine)
            addView(tRadioOval)
            addView(tRadioRectangle)
        }

        tColorPickerLine = ColorPicker(ctx).apply {
            id = View.generateViewId()
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = DEFAULT_SIZE_COLOR_PICKER.toPixel()
                addRule(BELOW, tRadioOption.id)
            }
            onEventChangeColorPicker = {
                tBrushDraw.color = it
                tSampleLine.setBackgroundColor(it)
                onEventChangeColor?.invoke(it)
            }
        }

        tSeekSizeLine = SeekBar(ctx).apply {
            id = View.generateViewId()
            max = MAXIMUM_SIZE_DRAW
            min = MINIMUM_SIZE_DRAW

            progress = (DEFAULT_SIZE_DRAW).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (seekBar == null) {
                        tBrushDraw.strokeWidth = DEFAULT_SIZE_DRAW
                        return
                    }
                    val progress = seekBar.progress / FACTOR_SIZE_DRAW
//                    onEventChangeSize?.invoke(seekBar.progress.toFloat())
                    onEventChangeSize?.invoke(progress)
                    tBrushDraw.strokeWidth = progress
//                    tBrushDraw.strokeWidth = when (seekBar != null) {
////                        true -> seekBar.progress.toFloat() / FACTOR_SIZE_DRAW
//                        true -> progress
//                        false -> DEFAULT_SIZE_DRAW
//                    }
//                    tSampleLine.text =
//                        tBrushDraw.strokeWidth.toFormat(DECIMAL_SHOW_SIZE_DRAW)
//                            .toEditable()
                    tSampleLine.text = progress.toFormat(DECIMAL_SHOW_SIZE_DRAW).toEditable()

                }
            })
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        tSampleLine = TextView(ctx).apply {
            id = View.generateViewId()
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
                tBrushDraw.strokeWidth.toFormat(DECIMAL_SHOW_SIZE_DRAW)
                    .toEditable()
            setBackgroundColor(tColorPickerLine.color)

        }
        val viewSizeLine = LinearLayout(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = DEFAULT_SIZE_LINE.toPixel()
                gravity = CENTER
                addRule(BELOW, tColorPickerLine.id)
            }
            orientation = LinearLayout.HORIZONTAL
            addView(tSampleLine)
            addView(tSeekSizeLine)
        }
        this.apply {
            addView(tRadioOption)
            addView(tColorPickerLine)
            addView(viewSizeLine)
        }
        //endregion
    }

    private fun getShapeName(type: ShapeType): String =
        when (type) {
            ShapeType.BRUSH -> "Brush"
            ShapeType.OVAL -> "Oval"
            ShapeType.LINE -> "Line"
            ShapeType.RECTANGLE -> "Rectangle"
        }

    private fun getRadioButton(type: ShapeType, checked: Boolean = false): RadioButton? {
        if (dbShapeId.any { it.first == type }) return null
        val result = RadioButton(ctx).apply {
            id = View.generateViewId()
            isChecked = checked
            text = getShapeName(type)
            setTextColor(Color.BLACK)
        }
        dbShapeId.add(type to result.id)
        return result
    }

    //endregion

    companion object {
        const val MINIMUM_SIZE_DRAW: Int = 50
        const val MAXIMUM_SIZE_DRAW: Int = 300
        const val FACTOR_SIZE_DRAW: Float = 10F
        const val DEFAULT_SIZE_DRAW: Float = 1F
        const val DECIMAL_SHOW_SIZE_DRAW: Int = 1

        const val DEFAULT_SIZE_COLOR_PICKER: Int = 65
        const val DEFAULT_SIZE_LINE: Int = 36
        const val DEFAULT_SIZE_SHAPE: Int = 24

        const val MARGIN_BOTTOM_NORMAL = 100
        const val MARGIN_BOTTOM_DRAW = 375
    }
}