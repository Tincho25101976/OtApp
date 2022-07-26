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
    var onEventSelectAction: ((TypeShapeAction) -> Unit)? = null
    //endregion

    //region properties
    private var tColorPickerLine: ColorPicker?
    private var tSeekSizeLine: SeekBar?
    private lateinit var tSampleLine: TextView
    private var tRadioOption: RadioGroup
    private var tRadioBrush: RadioButton?
    private var tRadioOval: RadioButton?
    private var tRadioLine: RadioButton?
    private var tRadioRectangle: RadioButton?
    private var tUndo: ImageView?
    private var tRedo: ImageView?

    private val dbShapeId: MutableList<Pair<ShapeType, Int>> = mutableListOf()

    var colorSelect: Int = DEFAULT_COLOR_SELECT
        private set
    var sizeLineSelect: Float = DEFAULT_SIZE_DRAW
        private set
    var shapeSelect: ShapeType = ShapeType.BRUSH
        private set

    private val lineSizeCalc = { value: Int -> value / FACTOR_SIZE_DRAW }
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
                if (dbShapeId.any { it.second == id }) {
                    this@ShapeCustomSelect.shapeSelect = dbShapeId.first { it.second == id }.first
                    onEventChangeShape?.invoke(this@ShapeCustomSelect.shapeSelect)
                }
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
            layoutParams = HelperUI.makeCustomLayoutLinealLayout().apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                weight = 7F
            }
            progress = arrayOf(max, min).average().toInt()

        }
        tSampleLine = TextView(ctx).apply {
            id = View.generateViewId()
            text = null
            setBackgroundColor(Color.WHITE)
            layoutParams = HelperUI.makeCustomLayoutLinealLayout().apply {
                height = DEFAULT_SIZE_SAMPLE.toPixel()
                width = DEFAULT_SIZE_SAMPLE.toPixel()
                weight = 1F
            }
            background = ctx.getDrawable(R.drawable.border_view)
            gravity = CENTER
            textSize = DEFAULT_SIZE_TEXT.toPixel().toFloat()

            setBackgroundColor(tColorPickerLine!!.color)
        }
        tUndo = ImageView(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutLinealLayout().apply {
                height = SIZE_CMD_SUCCESS.toPixel()
                width = SIZE_CMD_SUCCESS.toPixel()
                setMargins(5.toPixel())
                weight = 1F
            }
            setImageDrawable(ctx.getDrawable(R.drawable.pic_undo))
            setOnClickListener { onEventSelectAction?.invoke(TypeShapeAction.UNDO) }
        }
        tRedo = ImageView(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutLinealLayout().apply {
                height = SIZE_CMD_SUCCESS.toPixel()
                width = SIZE_CMD_SUCCESS.toPixel()
                setMargins(5.toPixel())
                weight = 1F
            }
            setImageDrawable(ctx.getDrawable(R.drawable.pic_redo))
            setOnClickListener { onEventSelectAction?.invoke(TypeShapeAction.REDO) }
        }

        val viewSizeLine = LinearLayout(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = DEFAULT_SIZE_LINE.toPixel()
                gravity = CENTER
                weightSum = 10F
                addRule(BELOW, tColorPickerLine!!.id)
            }
            orientation = LinearLayout.HORIZONTAL
            addView(tSampleLine)
            addView(tSeekSizeLine!!)
            addView(tUndo!!)
            addView(tRedo!!)
        }

        setTextLineSize()
        this.apply {
            addView(tRadioOption)
//            addView(viewAction)
            addView(tColorPickerLine)
            addView(viewSizeLine)
            setTextLineSize()
        }
        //endregion
    }

    private fun getLinealLayout(view: View, weight: Float = 1.0F): LinearLayout {
        val viewSizeLine = LinearLayout(ctx).apply {
            layoutParams = HelperUI.makeCustomLayoutLinealLayout().apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                gravity = CENTER
                this.weight = weight
            }
            orientation = LinearLayout.VERTICAL
            addView(view)
        }
        return viewSizeLine
    }

    private fun setTextLineSize() {
        if (tSeekSizeLine == null || !this::tSampleLine.isLateinit) return
        tSampleLine.text = getSizeLineResult().toFormat(DECIMAL_SHOW_SIZE_DRAW).toEditable()
    }

    private fun getSizeLineResult(): Float {
        if (tSeekSizeLine == null) return DEFAULT_SIZE_DRAW
        val value = tSeekSizeLine!!.progress
        val result = lineSizeCalc(value)
        return result
    }

    private fun getColorResult(): Int =
        (tColorPickerLine == null) then DEFAULT_COLOR_SELECT or tColorPickerLine!!.color

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

    fun setTypeFace(value: Typeface) {
        if (!this::tSeekSizeLine.isLateinit) tSampleLine.typeface = value
        arrayOf(tRadioLine, tRadioBrush, tRadioOval, tRadioRectangle).filterNotNull()
            .forEach { it.typeface = value }
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

        const val DEFAULT_COLOR_SELECT = Color.YELLOW

        const val SIZE_CMD_SUCCESS: Int = 28
    }
}