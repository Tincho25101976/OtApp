package com.vsg.helper.ui.widget.imageView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class DrawLineCanvas @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(ctx, attrs, defStyleAttr) {

    private lateinit var currentCanvas: Canvas
    private var currentBrush: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }
    private var currentPaintBackground: Paint = Paint().apply { color = Color.WHITE }
    private var currentTouchPath: Path = Path()
    private var currentBitmap: Bitmap? = null
    private var layerBitmap: Bitmap? = null

    //region fit
//    private var mSaveScale = 1f
//    private var viewWidth = 0
//    private var viewHeight = 0
//    private var origWidth = 0f
//    private var origHeight = 0f
//    private var mMatrix: Matrix = Matrix()
    //endregion

    //region constructor
//    constructor (context: Context) : super(context)
//    constructor (context: Context, paint: Paint) : super(context) {
//        this.currentBrush = paint
//    }
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        currentTouchPath = Path()
//    }
//    constructor (context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
//        context,
//        attrs,
//        defStyleAttr
//    )
//    init {
////        imageMatrix = mMatrix
//    }
    //endregion

    fun setBrushToDraw(brush: Paint) {
        currentBrush = brush
    }

    override fun setImageBitmap(bitmap: Bitmap?) {
        if (bitmap == null) return
        super.setImageBitmap(bitmap)
        currentBitmap = bitmap
//        currentCanvas = Canvas(currentBitmap!!)
    }

    fun getBitmap(): Bitmap? {
        return currentBitmap
    }

//    private fun fitToScreen() {
//        mSaveScale = 1f
//        val scale: Float
//        val drawable = drawable
//        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
//        val imageWidth = drawable.intrinsicWidth
//        val imageHeight = drawable.intrinsicHeight
//        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
//        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
//        scale = scaleX.coerceAtMost(scaleY)
//        mMatrix.setScale(scale, scale)
//
//        // Center the image
//        var redundantYSpace = (viewHeight.toFloat()
//                - scale * imageHeight.toFloat())
//        var redundantXSpace = (viewWidth.toFloat()
//                - scale * imageWidth.toFloat())
//        redundantYSpace /= 2.toFloat()
//        redundantXSpace /= 2.toFloat()
//        mMatrix.postTranslate(redundantXSpace, redundantYSpace)
//        origWidth = viewWidth - 2 * redundantXSpace
//        origHeight = viewHeight - 2 * redundantYSpace
//        imageMatrix = mMatrix
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
//        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
//        if (mSaveScale == 1f) {
//            // Fit to screen.
//            fitToScreen()
//        }
//    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layerBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        currentCanvas = Canvas(layerBitmap!!).apply {
            drawBitmap(currentBitmap!!, 0F, 0F, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> currentTouchPath.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> currentTouchPath.lineTo(touchX, touchY)
            MotionEvent.ACTION_UP -> {
                currentTouchPath.lineTo(touchX, touchY)
                currentCanvas.drawPath(currentTouchPath, currentBrush)
                currentTouchPath = Path()
            }
            else -> return false
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (layerBitmap == null) return
        canvas.drawBitmap(layerBitmap!!, 0f, 0f, currentPaintBackground)
        canvas.drawPath(currentTouchPath, currentBrush)
    }
}