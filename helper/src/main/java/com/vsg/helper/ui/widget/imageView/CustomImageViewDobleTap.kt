package com.vsg.helper.ui.widget.imageView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class CustomImageViewDobleTap @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(ctx, attrs, defStyleAttr),
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private var mGestureDetector: GestureDetector
    var onEventDoubleTap: ((AppCompatImageView, Bitmap?) -> Unit)? = null

    init {
        isClickable = true
        mGestureDetector = GestureDetector(ctx, this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
    //endregion

    //region double tap
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean = false
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        val temp: Bitmap? = when (drawable == null) {
            true -> null
            false -> (drawable as BitmapDrawable).bitmap
        }
        onEventDoubleTap?.invoke(this, temp)
        return false
    }
    override fun onDoubleTapEvent(e: MotionEvent?): Boolean = false
    //endregion

    //region listener
    override fun onDown(e: MotionEvent?): Boolean =false
    override fun onShowPress(e: MotionEvent?) {    }
    override fun onSingleTapUp(e: MotionEvent?): Boolean =false
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean = false
    override fun onLongPress(e: MotionEvent?) {    }
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean = false
    //endregion
}