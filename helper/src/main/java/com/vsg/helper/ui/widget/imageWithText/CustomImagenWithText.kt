package com.vsg.helper.ui.widget.imageWithText

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.resize
import com.vsg.helper.helper.customView.HelperCustomView.Companion.getStringOrDefault
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.helper.type.TypeMakeLayoutParameter

class CustomImagenWithText @JvmOverloads constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr) {

    //region field
    private var tImage: ImageView? = null
    private var tText: TextView? = null
    //endregion

    //region init
    init {
        val sizeImage: Int = DEFAULT_VALUE_SIZE_IMAGE.toPixel(ctx)
        this.layoutParams = HelperUI.makeCustomLayoutLinealLayout(
            TypeMakeLayoutParameter.CUSTOM,
            0,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            weight = 1F
        }
        gravity = Gravity.CENTER
//        id = View.generateViewId()

        tImage = ImageView(ctx).apply {
            layoutParams = makeCustomLayoutRelativeLayout(
                TypeMakeLayoutParameter.CUSTOM,
                sizeImage,
                sizeImage
            ).apply {
                addRule(RelativeLayout.CENTER_HORIZONTAL)
            }
            id = View.generateViewId()
        }

        tText = TextView(ctx).apply {
            layoutParams =
                makeCustomLayoutRelativeLayout(TypeMakeLayoutParameter.WIDTH_WRAP_HEIGHT_WRAP).apply {
                    addRule(BELOW, tImage?.id!!)
                    addRule(CENTER_HORIZONTAL)
                }
            text = DEFAULT_VALUE_TEXT
            id = View.generateViewId()
        }

        addView(tImage)
        addView(tText)

        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.CustomImagenWithText,
            0, 0
        )
        // Extract custom attributes into member variables
        customSizeImage =
            typedArray.getInt(
                R.styleable.CustomImagenWithText_customSizeImage,
                DEFAULT_VALUE_SIZE_IMAGE
            )
        customResourceImage =
            typedArray.getResourceId(
                R.styleable.CustomImagenWithText_customResourceImage,
                0
            )

        customText = typedArray.getStringOrDefault(R.styleable.CustomImagenWithText_customText)

        customResourceString =
            typedArray.getResourceId(
                R.styleable.CustomImagenWithText_customResourceString,
                0
            )
        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }
    //endregion

    //region properties
    @IntRange(from = 16, to = 1024)
    var customSizeImage: Int = DEFAULT_VALUE_SIZE_IMAGE
        set(value) {
            if (tImage == null) return
            tImage?.resize(value.toPixel(ctx))
            field = value
        }

    @DrawableRes
    var customResourceImage: Int = 0
        set(value) {
            if (tImage == null || value <= 0) return
            tImage?.setImageBitmap(BitmapFactory.decodeResource(ctx.resources, value))
            field = value
        }

    var customText: String = DEFAULT_VALUE_TEXT
        get() = when (tText == null) {
            true -> ""
            false -> tText?.text!!.toString()
        }
        set(value) {
            if (tText == null) return
            tText?.text = value
            field = value
        }

    @StringRes
    var customResourceString: Int = 0
        set(value) {
            if (tText == null) return
            tText?.text = ctx.resources.getText(value)
            field = value
        }
    //endregion

    companion object {
        private const val DEFAULT_VALUE_SIZE_IMAGE: Int = 48
        private const val DEFAULT_VALUE_TEXT: String = "..."
    }
}