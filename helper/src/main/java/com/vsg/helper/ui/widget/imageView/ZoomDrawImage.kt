package com.vsg.helper.ui.widget.imageView

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.vsg.helper.helper.HelperUI.Static.getBitmap

class ZoomDrawImage @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(ctx, attrs, defStyleAttr), View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private var mType: ZoomDrawImageActionType = ZoomDrawImageActionType.ZOOM
    var type: ZoomDrawImageActionType
        get() = mType
        set(value) {
            when (value) {
                ZoomDrawImageActionType.DRAW -> {
//                    mode = ZoomDrawImageType.NONE
//                    val w = drawable.intrinsicWidth
//                    val h = drawable.intrinsicHeight
//                    currentBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888, true)
                    currentBitmap =
                        (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    currentCanvas = Canvas(currentBitmap!!)
                    fitToScreen()
//                    currentCanvas = Canvas().apply {
//                        drawBitmap(currentBitmap!!, mMatrix!!, currentBrush)
//                    }
                }
                else -> Unit
            }
            mType = value
        }

    //region shared constructing
    private var mContext: Context? = null
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mGestureDetector: GestureDetector? = null
    private var mMatrix: Matrix? = null
    private var mMatrixValues: FloatArray? = null
    private var mode: ZoomDrawImageType = ZoomDrawImageType.NONE
    //endregion

    //region  Scales
    private var mSaveScale = 1f
    private var mMinScale = 1f
    private var mMaxScale = 4f
    //endregion

    //region  view dimensions
    private var origWidth = 0f
    private var origHeight = 0f
    private var viewWidth = 0
    private var viewHeight = 0
    private var mLast = PointF()
    private var mStart = PointF()
    //endregion

    //region draw
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
    //endregion

    init {
        sharedConstructing(ctx)
    }

    //region draw
    fun setBrushToDraw(brush: Paint) {
        currentBrush = brush
    }

    override fun setImageBitmap(bitmap: Bitmap?) {
        if (bitmap == null) return
        super.setImageBitmap(bitmap)
//        val w = drawable.intrinsicWidth
//        val h = drawable.intrinsicHeight
        currentBitmap = bitmap
        currentCanvas = Canvas(currentBitmap!!)
    }

    fun getImagenBitmap(): Bitmap? {
        val temp = when (type) {
            ZoomDrawImageActionType.ZOOM -> getBitmap()
            ZoomDrawImageActionType.DRAW -> {
                val w = drawable.intrinsicWidth
                val h = drawable.intrinsicHeight
//                val tempMatrix: Matrix = Matrix()
//                val tempPaint: Paint = Paint().apply { color = Color.TRANSPARENT }
                val finalBitmap: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                val tempBitmap: Bitmap? =
                    (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val tempCanvas: Canvas = Canvas(finalBitmap)
                tempCanvas.drawBitmap(tempBitmap!!, 0F, 0F, null)
                tempCanvas.drawBitmap(currentBitmap!!, 0F, 0F, null)
                currentBitmap

            }
        }
        return temp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (type) {
            ZoomDrawImageActionType.DRAW -> {
                if (currentBitmap == null) return
                canvas.drawBitmap(currentBitmap!!, 0f, 0f, currentPaintBackground)
                canvas.drawPath(currentTouchPath, currentBrush)
            }
            else -> Unit
        }
    }
    //endregion

    private fun sharedConstructing(context: Context) {
        super.setClickable(true)
        mContext = context
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mMatrix = Matrix()
        mMatrixValues = FloatArray(9)
        imageMatrix = mMatrix
        scaleType = ScaleType.MATRIX
        mGestureDetector = GestureDetector(context, this)
        setOnTouchListener(this)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = when (type) {
                ZoomDrawImageActionType.ZOOM -> ZoomDrawImageType.ZOOM
                ZoomDrawImageActionType.DRAW -> ZoomDrawImageType.DRAG
            }
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val prevScale = mSaveScale
            mSaveScale *= mScaleFactor
            if (mSaveScale > mMaxScale) {
                mSaveScale = mMaxScale
                mScaleFactor = mMaxScale / prevScale
            } else if (mSaveScale < mMinScale) {
                mSaveScale = mMinScale
                mScaleFactor = mMinScale / prevScale
            }
            if (origWidth * mSaveScale <= viewWidth || origHeight * mSaveScale <= viewHeight
            ) {
                mMatrix!!.postScale(
                    mScaleFactor, mScaleFactor, viewWidth / 2.toFloat(),
                    viewHeight / 2.toFloat()
                )
            } else {
                mMatrix!!.postScale(
                    mScaleFactor, mScaleFactor,
                    detector.focusX, detector.focusY
                )
            }
            fixTranslation()
            return true
        }
    }

    private fun fitToScreen() {
        mSaveScale = 1f
        val scale: Float
        val drawable = drawable
        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
        val imageWidth = drawable.intrinsicWidth
        val imageHeight = drawable.intrinsicHeight
        val scaleX = viewWidth.toFloat() / imageWidth.toFloat()
        val scaleY = viewHeight.toFloat() / imageHeight.toFloat()
        scale = scaleX.coerceAtMost(scaleY)
        mMatrix!!.setScale(scale, scale)

        // Center the image
        var redundantYSpace = (viewHeight.toFloat()
                - scale * imageHeight.toFloat())
        var redundantXSpace = (viewWidth.toFloat()
                - scale * imageWidth.toFloat())
        redundantYSpace /= 2.toFloat()
        redundantXSpace /= 2.toFloat()
        mMatrix!!.postTranslate(redundantXSpace, redundantYSpace)
        origWidth = viewWidth - 2 * redundantXSpace
        origHeight = viewHeight - 2 * redundantYSpace
        imageMatrix = mMatrix
//        currentCanvas.setMatrix(mMatrix)
    }

    fun fixTranslation() {
        mMatrix!!.getValues(mMatrixValues) //put matrix values into a float array so we can analyze
        val transX =
            mMatrixValues!![Matrix.MTRANS_X] //get the most recent translation in x direction
        val transY =
            mMatrixValues!![Matrix.MTRANS_Y] //get the most recent translation in y direction
        val fixTransX = getFixTranslation(transX, viewWidth.toFloat(), origWidth * mSaveScale)
        val fixTransY = getFixTranslation(transY, viewHeight.toFloat(), origHeight * mSaveScale)
        if (fixTransX != 0f || fixTransY != 0f) mMatrix!!.postTranslate(fixTransX, fixTransY)
    }

    private fun getFixTranslation(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) { // case: NOT ZOOMED
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else { //CASE: ZOOMED
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) { // negative x or y translation (down or to the right)
            return -trans + minTrans
        }
        if (trans > maxTrans) { // positive x or y translation (up or to the left)
            return -trans + maxTrans
        }
        return 0F
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        return if (contentSize <= viewSize) {
            0F
        } else delta
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (mSaveScale == 1f) {
            // Fit to screen.
            fitToScreen()
        }
    }

    //region OnTouch
    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        if (type == ZoomDrawImageActionType.ZOOM) {
            mScaleDetector!!.onTouchEvent(event)
            mGestureDetector!!.onTouchEvent(event)
            val currentPoint = PointF(event.x, event.y)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mLast.set(currentPoint)
                    mStart.set(mLast)
                    mode = ZoomDrawImageType.DRAG
                }
                MotionEvent.ACTION_MOVE -> {
                    if (mode == ZoomDrawImageType.DRAG) {
                        val dx = currentPoint.x - mLast.x
                        val dy = currentPoint.y - mLast.y
                        val fixTransX =
                            getFixDragTrans(dx, viewWidth.toFloat(), origWidth * mSaveScale)
                        val fixTransY =
                            getFixDragTrans(dy, viewHeight.toFloat(), origHeight * mSaveScale)
                        mMatrix!!.postTranslate(fixTransX, fixTransY)
                        fixTranslation()
                        mLast[currentPoint.x] = currentPoint.y
                    }
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    mode = ZoomDrawImageType.NONE
                }
            }
//            currentCanvas.setMatrix(mMatrix)
            imageMatrix = mMatrix
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (type == ZoomDrawImageActionType.DRAW) {
            val touchX = event.x
            val touchY = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> currentTouchPath.moveTo(touchX, touchY)
                MotionEvent.ACTION_MOVE -> currentTouchPath.lineTo(touchX, touchY)
                MotionEvent.ACTION_UP -> {
                    currentTouchPath.lineTo(touchX, touchY)
                    currentCanvas.drawPath(currentTouchPath, currentBrush)
                    mMatrix?.mapPoints(floatArrayOf(touchX, touchY))
                    currentTouchPath = Path()
                }
                else -> return false
            }
            invalidate()
        }
        return true
    }
    //endregion

    //region GestureListener
    override fun onDown(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(motionEvent: MotionEvent) {}
    override fun onSingleTapUp(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        motionEvent: MotionEvent,
        motionEvent1: MotionEvent,
        v: Float,
        v1: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(motionEvent: MotionEvent) {

    }

    override fun onFling(
        motionEvent: MotionEvent,
        motionEvent1: MotionEvent,
        v: Float,
        v1: Float
    ): Boolean {
        return false
    }
    //endregion

    //region onDoubleTap
    override fun onSingleTapConfirmed(motionEvent: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(motionEvent: MotionEvent): Boolean {
        fitToScreen()
        return false
    }

    override fun onDoubleTapEvent(motionEvent: MotionEvent): Boolean {
        return false
    }
    //endregion
}