package com.vsg.helper.ui.widget.imageView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.vsg.helper.helper.HelperUI.Static.setPictureFromBitmap

@SuppressLint("AppCompatCustomView")
class DrawLineImage @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(ctx, attrs, defStyleAttr) {

    //region property
    private var PointX: Float = 0F
    private var PointY: Float = 0F
    private var canvasBitmap: Bitmap? = null
    private var currentCanvas: Canvas? = null
    private var currentBitmap: Bitmap? = null
    private var currentTouchPath: Path = Path()
    private var currentPaintBackground: Paint = Paint(Paint.DITHER_FLAG)
    private var currentBrush: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12F
    }
    private val currentY: Float
        get() = when (this.height > 0 && currentBitmap != null) {
            true -> ((this.height / 2) - (currentBitmap!!.height / 2)).toFloat()
            false -> 0F
        }
    //endregion

    //region draw
    fun setBrushToDraw(brush: Paint) {
        currentBrush = brush
    }
    //endregion

    override fun setImageBitmap(bm: Bitmap?) {
        if (bm == null) return
        super.setImageBitmap(bm)
    }
    fun getBitmap(): Bitmap? = currentBitmap

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.layoutParams.apply {
            height = h
            width = w
        }
        currentBitmap = this.setPictureFromBitmap((drawable as BitmapDrawable).bitmap)
        super.setImageBitmap(currentBitmap)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        currentCanvas = Canvas(currentBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap!!, 0f, 0F, currentPaintBackground)
        canvas.drawPath(currentTouchPath, currentBrush)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> currentTouchPath.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> currentTouchPath.lineTo(touchX, touchY)
            MotionEvent.ACTION_UP -> {
                currentTouchPath.lineTo(touchX, touchY)
                currentCanvas?.drawPath(currentTouchPath, currentBrush)
                currentTouchPath = Path()
            }
            else -> return false
        }
        invalidate()
        return true
    }
}