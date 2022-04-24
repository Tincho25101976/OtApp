package com.vsg.helper.ui.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.children

open class RelativeLayoutWithDisableSupport @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRest) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        this.disabled = !enabled
        this.disabledControls(enabled, this)
    }
    var disabled = false
        set(value) {
            field = value
            requestLayout()
        }

    private val paint = Paint()

    init {
        val cm = ColorMatrix()
        cm.set(
            floatArrayOf(
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        paint.colorFilter = ColorMatrixColorFilter(cm)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        if (disabled) {
            canvas?.saveLayer(null, paint)
        }
        super.dispatchDraw(canvas)
        if (disabled) {
            canvas?.restore()
        }
    }

    override fun draw(canvas: Canvas?) {
        if (disabled) {
            canvas?.saveLayer(null, paint)
        }
        super.draw(canvas)
        if (disabled) {
            canvas?.restore()
        }
    }

    private fun disabledControls(enabled: Boolean, gr: ViewGroup){
        gr.children.forEach {
            it.isEnabled = enabled
            if(it is ViewGroup) disabledControls(enabled, it)
        }
    }
}