package com.vsg.helper.ui.widget.imageView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class DrawableView : View {
    var currentWidth: Int = 0
    var currentHeight: Int = 0
    private var isEditable = false
    private lateinit var drawPath: Path
    private lateinit var drawPaint: Paint
    private var canvasPaint: Paint = Paint(Paint.DITHER_FLAG)
    private lateinit var drawCanvas: Canvas
    private lateinit var canvasBitmap: Bitmap
    private val paintColor: Int = Color.RED

    constructor (context: Context?) : super(context)
    constructor (context: Context?, attrs: AttributeSet) : super(context, attrs) {
        setupDrawing()
    }

    constructor (context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentHeight = h
        currentWidth = w
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
    }

    private fun setupDrawing() {
        drawPath = Path()
        drawPaint = Paint().apply {
            color = paintColor
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 10F
        }
    }

    fun setDrawingEnabled(isEditable: Boolean) {
        this.isEditable = isEditable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap, 0F, 0F, canvasPaint)
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEditable) {
            val touchX = event.x
            val touchY = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> drawPath.moveTo(touchX, touchY)
                MotionEvent.ACTION_MOVE -> drawPath.lineTo(touchX, touchY)
                MotionEvent.ACTION_UP -> {
                    drawPath.lineTo(touchX, touchY)
                    drawCanvas.drawPath(drawPath, drawPaint)
                    drawPath = Path()
                }
                else -> return false
            }
        } else {
            return false
        }
        invalidate()
        return true
    }
}