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
import androidx.core.view.setMargins
import com.vsg.helper.R
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
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
    private lateinit var tColorPickerLine: ColorPicker
    private lateinit var tSeekSizeLine: SeekBar
    private lateinit var tSampleLine: TextView
    private var tRadioOption: RadioGroup
    private var tRadioBrush: RadioButton?
    private var tRadioOval: RadioButton?
    private var tRadioLine: RadioButton?
    private var tRadioRectangle: RadioButton?


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
                height = ViewGroup.LayoutParams.WRAP_CONTENT
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
                tSampleLine.setBackgroundColor(it)
                onEventChangeColor?.invoke(it)
            }
        }

        tSeekSizeLine = SeekBar(ctx).apply {
            id = View.generateViewId()
            max = MAXIMUM_SIZE_DRAW
            min = MINIMUM_SIZE_DRAW
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (seekBar == null) return
                    val progress = lineSizeCalc(seekBar.progress)
                    onEventChangeSize?.invoke(progress)
                    setTextLineSize()
                }
            })
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            progress = arrayOf(max, min).average().toInt()
        }
        tSampleLine = TextView(ctx).apply {
            id = View.generateViewId()
            text = null
            setBackgroundColor(Color.WHITE)
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = DEFAULT_SIZE_SAMPLE.toPixel()
                width = DEFAULT_SIZE_SAMPLE.toPixel()
            }
            background = ctx.getDrawable(R.drawable.border_view)
            gravity = CENTER
            textSize = DEFAULT_SIZE_TEXT.toPixel().toFloat()
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

        setTextLineSize()
        this.apply {
            addView(tRadioOption)
            addView(tColorPickerLine)
            addView(viewSizeLine)
            setTextLineSize()
        }
        //endregion
    }

    val lineSizeCalc = { value: Int -> value / FACTOR_SIZE_DRAW }
    private fun setTextLineSize() {
        if (!this::tSeekSizeLine.isLateinit || !this::tSampleLine.isLateinit) return
        tSampleLine.text = getSizeLineResult().toFormat(DECIMAL_SHOW_SIZE_DRAW).toEditable()
    }

    private fun getSizeLineResult(): Float {
        if (this::tSeekSizeLine.isLateinit) return DEFAULT_SIZE_DRAW
        return lineSizeCalc(tSeekSizeLine.progress)
    }

    private fun getColorResult(): Int =
        (this::tColorPickerLine.isLateinit) then Color.GREEN or tColorPickerLine.color

    private fun getPaintResult(): Paint {
        return Paint().apply {
            color = getColorResult()
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = getSizeLineResult()
        }
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

    //endregion

    companion object {
        const val MINIMUM_SIZE_DRAW: Int = 50
        const val MAXIMUM_SIZE_DRAW: Int = 300
        const val FACTOR_SIZE_DRAW: Float = 10F
        const val DEFAULT_SIZE_DRAW: Float = 12F
        const val DECIMAL_SHOW_SIZE_DRAW: Int = 1

        const val DEFAULT_SIZE_COLOR_PICKER: Int = 60
        const val DEFAULT_SIZE_LINE: Int = 24
        const val DEFAULT_SIZE_SHAPE: Int = 24
        const val DEFAULT_SIZE_SAMPLE: Int = 32
        const val DEFAULT_SIZE_TEXT: Int = 7
    }
}