package com.vsg.helper.ui.widget.imageView

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener

class CustomImageView : androidx.appcompat.widget.AppCompatImageView {
    private lateinit var currentMatrix: Matrix

    // We can be in one of these 3 states
    val NONE: Int = 0
    val DRAG: Int = 1
    val ZOOM: Int = 2
    var mode: Int = NONE
    private var zoomEnable: Boolean = true

    // Remember some things for zooming
    var last = PointF()
    var start = PointF()
    var minScale = 1f
    var maxScale = 5f
    private lateinit var m: FloatArray

    var viewWidth: Int = 0
    var viewHeight: Int = 0
    val CLICK = 3
    var saveScale = 1f
    private var origWidth: Float = 0f
    private var origHeight: Float = 0f
    var oldMeasuredWidth: Int = 0
    var oldMeasuredHeight: Int = 0
    var mScaleDetector: ScaleGestureDetector? = null
    var currentContext: Context? = null

    constructor (context: Context) : super(context) {
        sharedConstructing(context)
    }

    fun setZoomEnable(status: Boolean) {
        zoomEnable = status
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        sharedConstructing(context)
    }

    private fun sharedConstructing(context: Context) {
        super.setClickable(true)
        this.currentContext = context
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        currentMatrix = Matrix()
        m = FloatArray(9)
        imageMatrix = currentMatrix
        scaleType = ScaleType.MATRIX
        setOnTouchListener { _, event ->
            if (zoomEnable) {
                mScaleDetector!!.onTouchEvent(event)
                val curr = PointF(event.x, event.y)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        last.set(curr)
                        start.set(last)
                        mode = DRAG
                    }
                    MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                        val deltaX = curr.x - last.x
                        val deltaY = curr.y - last.y
                        val fixTransX = getFixDragTrans(
                            deltaX, viewWidth.toFloat(),
                            origWidth * saveScale
                        )
                        val fixTransY = getFixDragTrans(
                            deltaY, viewHeight.toFloat(),
                            origHeight * saveScale
                        )
                        currentMatrix.postTranslate(fixTransX, fixTransY)
                        fixTrans()
                        last[curr.x] = curr.y
                    }
                    MotionEvent.ACTION_UP -> {
                        mode = NONE
                        val xDiff = Math.abs(curr.x - start.x).toInt()
                        val yDiff = Math.abs(curr.y - start.y).toInt()
                        if (xDiff < CLICK && yDiff < CLICK) performClick()
                    }
                    MotionEvent.ACTION_POINTER_UP -> mode = NONE
                }
                imageMatrix = currentMatrix
                invalidate()
                true // indicate event was handled
            } else {
                false
            }
        }
    }

    fun setMaxZoom(x: Float) {
        maxScale = x
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var mScaleFactor = detector.scaleFactor
            val origScale: Float = saveScale
            saveScale *= mScaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }
            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight
            ) matrix.postScale(
                mScaleFactor, mScaleFactor, (viewWidth / 2).toFloat(), (viewHeight / 2).toFloat()
            ) else matrix.postScale(
                mScaleFactor, mScaleFactor,
                detector.focusX, detector.focusY
            )
            fixTrans()
            return true
        }
    }

    fun fixTrans() {
        currentMatrix.getValues(m)
        val transX = m[Matrix.MTRANS_X]
        val transY = m[Matrix.MTRANS_Y]
        val fixTransX = getFixTrans(transX, viewWidth.toFloat(), origWidth * saveScale)
        val fixTransY = getFixTrans(
            transY, viewHeight.toFloat(), origHeight * saveScale
        )
        if (fixTransX != 0f || fixTransY != 0f) currentMatrix.postTranslate(fixTransX, fixTransY)
    }

    private fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float
        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }
        if (trans < minTrans) return -trans + minTrans
        return if (trans > maxTrans) -trans + maxTrans else 0F
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        return if (contentSize <= viewSize) 0F
        else delta
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight || viewWidth == 0 || viewHeight == 0) return
        oldMeasuredHeight = viewHeight
        oldMeasuredWidth = viewWidth
        if (saveScale == 1f) {
            // Fit to screen.
            val scale: Float
            val drawable = drawable
            if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) return
            val bmWidth = drawable.intrinsicWidth
            val bmHeight = drawable.intrinsicHeight
            Log.d("bmSize", "bmWidth: $bmWidth bmHeight : $bmHeight")
            val scaleX = viewWidth.toFloat() / bmWidth.toFloat()
            val scaleY = viewHeight.toFloat() / bmHeight.toFloat()
            scale = scaleX.coerceAtMost(scaleY)
            currentMatrix.setScale(scale, scale)

            // Center the image
            var redundantYSpace = (viewHeight.toFloat() - scale * bmHeight.toFloat())
            var redundantXSpace = (viewWidth.toFloat() - scale * bmWidth.toFloat())
            redundantYSpace /= 2.toFloat()
            redundantXSpace /= 2.toFloat()
            currentMatrix.postTranslate(redundantXSpace, redundantYSpace)
            origWidth = viewWidth - 2 * redundantXSpace
            origHeight = viewHeight - 2 * redundantYSpace
            imageMatrix = currentMatrix
        }
        fixTrans()
    }
}