package com.vsg.helper.ui.widget.text

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatEditText
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.setEditCustomLayoutRelativeLayout
import com.vsg.helper.helper.date.HelperDate.Companion.toDate
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.helper.type.TypeMakeLayoutParameter
import java.util.*
import kotlin.random.Random

class CustomEditText @JvmOverloads constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0
) : RelativeLayout(ctx, attrs, defStyleAttr, defStyleRest) {

    private var tEdit: EditText
    private var tTitle: TextView
    private var tContainer: RelativeLayout
    private val shape: GradientDrawable
        get() {
            return GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(backColor, backColor)
            ).apply {
                this.cornerRadii = (1..8).map { 4F }.toFloatArray()
                setStroke(
                    stroke.toPixel(context),
                    colorStroke
                )
            }
        }

    //region properties
    var backColor: Int = Color.WHITE
    var colorStroke: Int = Color.LTGRAY //Color.parseColor("#BF4B4BC8")
        set(value) {
            field = value
            tContainer.background = shape
        }
    var textSize: Float
        get() = tEdit.textSize
        set(value) {
            tEdit.textSize = value
        }
    var typeface: Typeface
        get() = tEdit.typeface
        set(value) {
            tEdit.typeface = value
        }
    var text: Editable?
        get() = tEdit.text
        set(value) {
            tEdit.text = value
        }
    var hint: CharSequence
        get() = tEdit.hint
        set(value) {
            tEdit.hint = value
            actionForHint()
        }
    var inputType: Int
        get() = tEdit.inputType
        set(value) {
            tEdit.inputType = value
        }
    var maxLines: Int
        get() = tEdit.maxLines
        set(value) {
            tEdit.maxLines = value
        }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        if(params != null) {
            tEdit.setEditCustomLayoutRelativeLayout().apply {
                height = params.height
                width = params.width
            }
        }
        super.setLayoutParams(params)
    }

    override fun setClickable(clickable: Boolean) {
        tEdit.isClickable = clickable
    }
    override fun setFocusable(focusable: Boolean) {
        tEdit.isFocusable = focusable
    }
    override fun setGravity(value: Int) {
        tEdit.gravity = value
    }

    override fun setEnabled(enabled: Boolean) {
        tEdit.isEnabled = enabled
    }

    var customMargin: Int = MARGIN_EDIT_TEXT
        get() = field.toPixel(ctx)

    @IntRange(from = 0, to = 10)
    var stroke: Int = 3
    //endregion

    //region fields
    private val title: Editable
        get() = when (tEdit.hint.isNullOrEmpty()) {
            true -> "".toEditable()
            false -> " ${tEdit.hint}:  ".toEditable()
        }

    private val marginEditTop: Int
        get() = MARGIN_EDIT_TEXT_TOP.toPixel(ctx)
    private val marginContainer: Int
        get() = MARGIN_CONTAINER.toPixel(ctx)
    private val marginTopContainer: Int
        get() = MARGIN_TOP_CONTAINER.toPixel(ctx)

    //endregion

    init {
        tEdit = AppCompatEditText(context).apply {

//            this.hint = TEXT_OF_TEST_HINT
//            this.text = TEXT_OF_TEST.toEditable()
//            setBackgroundColor(Color.parseColor("#80568709"))

            layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout()
                    .apply {
                        setMargins(customMargin, marginEditTop, customMargin, 0)
                        gravity = Gravity.CENTER
                    }
            gravity = Gravity.START
        }
        tTitle = TextView(ctx).apply {
            text = title
            textSize = 11F
            setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC)
            setBackgroundColor(backColor)
            layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout(TypeMakeLayoutParameter.WIDTH_WRAP_HEIGHT_WRAP)
                    .apply {
                        setMargins(35, -3, 0, 0)
                    }
        }
        tContainer = RelativeLayout(ctx).apply {
            this.background = shape
            this.layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout()
                    .apply {
                        setMargins(
                            marginContainer,
                            marginTopContainer,
                            marginContainer,
                            marginContainer
                        )
                    }
            this.addView(tEdit)

        }
        this.addView(tContainer)
        this.addView(tTitle)

        this.layoutParams =
            HelperUI.makeCustomLayoutRelativeLayout(TypeMakeLayoutParameter.WIDTH_WRAP_HEIGHT_WRAP)
                .apply {
                    height = tEdit.layoutParams.height
                    width = tEdit.layoutParams.width
                }
    }

    //region functional
    private fun actionForHint() {
        this.tTitle.text = title
    }

    fun toText(): String = tEdit.text.toString()
    fun toDouble(): Double = tEdit.text.toString().toDouble()
    fun toFloat(): Float = tEdit.text.toString().toFloat()
    fun toInt(): Int = tEdit.text.toString().toInt()
    fun toDate(): Date? =
        when (!this.tEdit.text.isNullOrEmpty()) {
            true -> this.tEdit.text.toString().toDate()
            false -> null
        }
    fun removeTextChangedListener(watcher: TextWatcher){
        tEdit.removeTextChangedListener(watcher)
    }
    fun addTextChangedListener(watcher: TextWatcher){
        tEdit.addTextChangedListener(watcher)
    }
    //endregion

    //region colors
    private fun getDefaultColors() =
        intArrayOf(0xFF4175A4.toInt(), 0xFF47A422.toInt(), 0xFFD9DC76.toInt(), 0xFFDC0A26.toInt())

    private fun getRandomInt(): Int {
        var result: Int
        do {
            result = Random.nextInt(0, Random.nextInt(1, 256))
        } while (result <= 0 || result > 256)
        return result
    }

    private fun randomColor(): Int {
        //(Math.random() * 16777215).toInt() or (0xFF shl 24)
        return Color.argb(255, getRandomInt(), getRandomInt(), getRandomInt())
    }

    private fun getColors(colors: Int = 4): IntArray {
        var qty = colors
        if (colors <= 0) qty = 2
        if (colors > 10) qty = 10
        val data: MutableList<Int> = mutableListOf()
        do {
            val temp = randomColor()
            if (!data.any { it == temp }) data.add(temp)
        } while (data.count() < qty)
        return data.toIntArray()
    }
    //endregion

    companion object {
        private const val TEXT_OF_TEST: String = "La puerca esta en la cocina..."
        private const val TEXT_OF_TEST_HINT: String = "Texto de prueba"
        private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

        private const val MARGIN_EDIT_TEXT: Int = 5
        private const val MARGIN_EDIT_TEXT_TOP: Int = 7
        private const val MARGIN_CONTAINER: Int = 1
        private const val MARGIN_TOP_CONTAINER: Int = 5
    }
}