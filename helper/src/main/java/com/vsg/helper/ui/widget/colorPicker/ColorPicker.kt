package com.vsg.helper.ui.widget.colorPicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.vsg.helper.R.styleable

@SuppressWarnings("MagicNumber")
class ColorPicker @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(ctx, attrs, defStyleAttr) {

    private val colors: IntArray
    private val strokeColor: Int

    private val strokeSize: Float
    private val radius: Float
    private val pickRadius: Float
    private val previewBaseline: Float
    private val rainbowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val rainbowBackgroundPaint by lazy { bgPaint() }
    private val pickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val previewRadius: Float
    var pick = 0.5f
    private val rainbowBaseline: Float
    var showPreview = false
    var listener: OnColorChangedListener? = null

    var onEventChangeColorPicker: ((Int) -> Unit)? = null

    init {
        val a = ctx.theme.obtainStyledAttributes(attrs, styleable.ColorPicker, defStyleAttr, 0)
        val resId = a.getResourceId(styleable.ColorPicker_picker_pallet, 0)
        colors = if (resId != 0) resources.getIntArray(resId) else defColors()
        //@formatter:off
        strokeColor = a.getColor(styleable.ColorPicker_picker_strokeColor, WHITE)
        strokeSize = a.getDimension(styleable.ColorPicker_picker_strokeSize, 2.dpToPx(ctx))
        radius = a.getDimension(styleable.ColorPicker_picker_radius, 16.dpToPx(ctx))
        pickRadius = a.getDimension(styleable.ColorPicker_picker_radius, 12.dpToPx(ctx))
        previewRadius = a.getDimension(styleable.ColorPicker_picker_previewRadius, 16.dpToPx(ctx))
        rainbowBaseline = a.getDimension(styleable.ColorPicker_picker_baseline, 48.dpToPx(ctx))
        previewBaseline =
            a.getDimension(styleable.ColorPicker_picker_previewBaseline, 18.dpToPx(ctx))
        //@formatter:on
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        drawPicker(canvas)
        drawColorAim(canvas, rainbowBaseline, pickRadius + strokeSize, pickRadius, color)
        if (showPreview) {
            drawColorAim(canvas, previewBaseline, previewRadius + strokeSize, previewRadius, color)
        }
    }

    private fun drawPicker(canvas: Canvas) {
        val lineX = radius.toFloat()
        val lineY = rainbowBaseline.toFloat()
        rainbowPaint.strokeWidth = radius.toFloat()
        rainbowBackgroundPaint.strokeWidth = rainbowPaint.strokeWidth + strokeSize
        canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowBackgroundPaint)
        canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowPaint)
    }

    private fun drawColorAim(
        canvas: Canvas,
        baseLine: Float,
        offset: Float,
        size: Float,
        color: Int
    ) {
        val circleCenterX = offset + pick * (canvas.width - offset * 2)
        canvas.drawCircle(
            circleCenterX,
            baseLine,
            size + strokeSize,
            pickPaint.apply {
                //this.color = strokeColor
                this.color = colorSample
                this.strokeWidth = 2F
                this.style = Paint.Style.FILL_AND_STROKE
                this.isDither = true
            })
        canvas.drawCircle(circleCenterX, baseLine, size, pickPaint.apply { this.color = color })
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight.toFloat()
        val width = measuredWidth.toFloat()
        val shader = LinearGradient(
            0.0f,
            height / 2.0f,
            width,
            height / 2.0f,
            colors,
            null,
            Shader.TileMode.CLAMP
        )
        rainbowPaint.shader = shader
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
            pick = event.x / measuredWidth.toFloat()
            if (pick < 0) pick = 0f
            else if (pick > 1) pick = 1f
            listener?.onColorChanged(color)
            onEventChangeColorPicker?.invoke(color)
            showPreview = true
        } else if (action == MotionEvent.ACTION_UP) {
            showPreview = false
        }
        postInvalidateOnAnimation()
        return true
    }

    val color: Int
        get() = interColor(pick, colors)
    val colorSample: Int
        get() = interColor(pick, defColorsSample())

    fun setOnColorChangedListener(listener: OnColorChangedListener) {
        this.listener = listener
    }

    private fun bgPaint(): Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = strokeColor
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    interface OnColorChangedListener {
        fun onColorChanged(color: Int)
    }

    companion object {
        private fun defColors() =
            intArrayOf(BLACK, GRAY, MAGENTA, RED, YELLOW, GREEN, BLUE, CYAN, WHITE)

        private fun defColorsSample() = intArrayOf(WHITE, BLACK)
    }
}