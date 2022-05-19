package com.vsg.helper.ui.widget.text

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import com.vsg.helper.R
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.helper.HelperUI.Static.addTextWatcher
import com.vsg.helper.helper.HelperUI.Static.getCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutLinealLayout
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.setEditCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.setTextWatcher
import com.vsg.helper.helper.HelperUI.Static.toEditable
import com.vsg.helper.helper.customView.HelperCustomView.Companion.getDate
import com.vsg.helper.helper.customView.HelperCustomView.Companion.getDoubleOrDefault
import com.vsg.helper.helper.customView.HelperCustomView.Companion.getEnum
import com.vsg.helper.helper.customView.HelperCustomView.Companion.getStringOrDefault
import com.vsg.helper.helper.date.HelperDate.Companion.toDate
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import com.vsg.helper.helper.mask.MaskType
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.ui.widget.ICustomViewSetTypeface
import com.vsg.helper.ui.widget.datePicker.DatePickerFragment
import com.vsg.helper.ui.widget.text.type.TypeCustomInputText
import com.vsg.helper.ui.widget.text.type.TypeCustomInputTextType
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import java.util.*
import kotlin.math.floor

class CustomInputText @JvmOverloads constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox,
    addId: Boolean = false
) : TextInputLayout(ctx, attrs, defStyleAttr), ICustomViewSetTypeface {

    //region field
    private var tEdit: TextInputEditText = TextInputEditText(ctx).apply {
        inputType = InputType.TYPE_CLASS_TEXT
        gravity = Gravity.START
        setHintTextColor(Color.LTGRAY)
    }
    private val textWatcher: TextWatcher = makeWatches()
    private var initTextFilter: Array<InputFilter>
    //endregion

    //region values
    val getEditText: TextInputEditText
        get() = tEdit
    val nameResource: String
        get() = try {
            resources.getResourceName(this.id)
        } catch (e: Exception) {
            ""
        }
    //endregion

    //region properties
    var customFormatDate: FormatDateString = DEFAULT_VALUE_DATE_FORMAT_STRING
    val typeCustomType: TypeCustomInputTextType
        get() = this.customType.type
    var customType: TypeCustomInputText = DEFAULT_VALUE_TYPE
        set(value) {
            if (value != field) {
                field = value
                isMultiline = value == TypeCustomInputText.TEXT_MULTILINE
                if (value == TypeCustomInputText.DOUBLE) setMask(MaskType.NUMBER_DECIMAL)
                if (value == TypeCustomInputText.INT) setMask(MaskType.NUMBER_INTEGER)
                if (value == TypeCustomInputText.PHONE) setMask(MaskType.PHONE)
                if (value == TypeCustomInputText.MAIL_ADDRESS) setMask(MaskType.MAIL_ADDRESS)
                if (value == TypeCustomInputText.DATE) setMask(MaskType.DATE)
                if (value == TypeCustomInputText.READ_ONLY_TEXT) {
                    isMultiline = false
                    isEnabled = false
                }
                if (value == TypeCustomInputText.READ_ONLY_MULTILINE) {
                    isMultiline = true
                    isEnabled = false
                }
            }
        }

    @IntRange(from = 0, to = 10)
    var customDecimalPlace: Int = DEFAULT_VALUE_DECIMAL_PLACE

    var textSize: Float
        get() = tEdit.textSize
        set(value) {
            tEdit.textSize = value
        }

    var text: String
        get() = tEdit.text.toString()
        set(value) {
            tEdit.text = value.toEditable()
        }
    var date: Date?
        get() = toDate()
        set(value) {
            text = when (value == null) {
                true -> ""
                false -> value.toDateString(customFormatDate)
            }
        }
    var double: Double?
        get() = toDouble()
        set(value) {
            text = when (value == null) {
                true -> ""
                false -> value.toFormat(customDecimalPlace)
            }
        }
    var int: Int?
        get() = toInt()
        set(value) {
            text = when (value == null) {
                true -> ""
                false -> value.toString()
            }
        }

    var inputType: Int
        get() = tEdit.inputType
        set(value) {
            tEdit.inputType = value
        }

    @IntRange(from = 1, to = 100)
    var customLines: Int = DEFAULT_VALUE_LINES

    @IntRange(from = 1, to = 100)
    var customMaxLines: Int = DEFAULT_VALUE_MAX_LINE

    @IntRange(from = 0, to = 1000)
    var customPadding: Int = DEFAULT_VALUE_PADDING
        set(value) {
            customSetPadding(value)
            field = value
        }

    @IntRange(from = 0, to = 1000)
    var customMargin: Int = DEFAULT_VALUE_MARGIN
        set(value) {
            customSetMargin(value)
            field = value
        }

    @IntRange(from = 0, to = 10000)
    var customMaxLength: Int = DEFAULT_VALUE_MAX_LENGTH
        set(value) {
            try {
                if (value == 0) {
                    tEdit.filters = initTextFilter
                } else {
                    val maxLengthFilter = LengthFilter(value)
                    val origin: Array<InputFilter> = tEdit.filters
                    val newFilters: Array<InputFilter?>
                    if (origin.isNotEmpty()) {
                        newFilters = arrayOfNulls(origin.size + 1)
                        System.arraycopy(origin, 0, newFilters, 0, origin.size)
                        newFilters[origin.size] = maxLengthFilter
                    } else {
                        newFilters = arrayOf(maxLengthFilter)
                    }
                    tEdit.filters = newFilters
                }
            } finally {
                field = value
            }
        }
    var customHintTextColor: Int = Color.GRAY
        set(value) {
            super.setHintTextColor(ColorStateList.valueOf(value))
        }

    private var isMultiline: Boolean = DEFAULT_VALUE_IS_MULTILINE
        set(value) {
            when (value) {
                true -> {
                    tEdit.isSingleLine = false
                    tEdit.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
                    tEdit.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    setLinesForMultilineText(this.customLines, this.customMaxLines)
                    tEdit.isVerticalScrollBarEnabled = value
                    tEdit.movementMethod = ScrollingMovementMethod.getInstance()
                    tEdit.scrollBarStyle = SCROLLBARS_INSIDE_INSET
                }
                false -> {
                    tEdit.isSingleLine = true
                    tEdit.imeOptions = EditorInfo.IME_FLAG_NAVIGATE_NEXT
                    tEdit.inputType = InputType.TYPE_CLASS_TEXT
                    tEdit.setLines(1)
                    tEdit.maxLines = 1
                    tEdit.isVerticalScrollBarEnabled = false
                    tEdit.movementMethod = null
                    tEdit.scrollBarStyle = SCROLLBARS_INSIDE_INSET
                }
            }
            field = value
        }
    //endregion

    //region init
    init {
        if (addId) this.id = ViewGroup.generateViewId()
        val m = customArrayParamLayout(DEFAULT_VALUE_MARGIN)
        val p = customArrayParamLayout(DEFAULT_VALUE_PADDING)
        initTextFilter = tEdit.filters
        this.apply {
            addView(tEdit, makeCustomLayoutLinealLayout().apply {
                setMargins(m[0], m[1], m[2], m[3])
                setPadding(p[0], p[1], p[2], p[3])
            })
            layoutParams = makeCustomLayoutRelativeLayout()
            boxBackgroundMode = BOX_BACKGROUND_OUTLINE
            val r = 25F
            setBoxCornerRadii(r, r, r, r)
            boxStrokeColor = Color.DKGRAY
            boxStrokeWidth = DEFAULT_STROKE_WIDTH
            defaultHintTextColor = ColorStateList.valueOf(Color.LTGRAY)

//            hint = TEXT_OF_TEST_HINT
            setHelperTextTextAppearance(R.style.HelperTextAppearance)
            setErrorTextAppearance(R.style.ErrorTextAppearance)
            setHintTextAppearance(R.style.HintTextAppearance)
            setCounterTextAppearance(R.style.CounterTextAppearance)
            setCounterOverflowTextAppearance(R.style.CounterOverFlowTextAppearance)
        }
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.CustomInputText,
            0, 0
        )
        // Extract custom attributes into member variables
        customType =
            typedArray.getEnum(
                R.styleable.CustomInputText_customType,
                DEFAULT_VALUE_TYPE,
                TypeCustomInputText::class.java
            )
        text = typedArray.getStringOrDefault(R.styleable.CustomInputText_customSetText)
        date = typedArray.getDate(R.styleable.CustomInputText_customSetDate)
        double =
            typedArray.getDoubleOrDefault(R.styleable.CustomInputText_customSetDouble, null)

        customDecimalPlace =
            typedArray.getInt(
                R.styleable.CustomInputText_customDecimalPlace,
                DEFAULT_VALUE_DECIMAL_PLACE
            )
        customMaxLength =
            typedArray.getInt(R.styleable.CustomInputText_customMaxLength, DEFAULT_VALUE_MAX_LENGTH)
        customMaxLines =
            typedArray.getInt(R.styleable.CustomInputText_customMaxLine, DEFAULT_VALUE_MAX_LINE)
        customLines =
            typedArray.getInt(R.styleable.CustomInputText_customLines, DEFAULT_VALUE_LINES)
        customFormatDate =
            typedArray.getEnum(
                R.styleable.CustomInputText_customFormatDate,
                DEFAULT_VALUE_DATE_FORMAT_STRING,
                FormatDateString::class.java
            )
        customPadding =
            typedArray.getInt(R.styleable.CustomInputText_customPadding, DEFAULT_VALUE_PADDING)
        customMargin =
            typedArray.getInt(R.styleable.CustomInputText_customMargin, DEFAULT_VALUE_MARGIN)

        setTextColor(
            typedArray.getColor(
                R.styleable.CustomInputText_customTextColor,
                DEFAULT_VALUE_TEXT_COLOR
            )
        )
        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }
    //endregion

    //region override
    override fun setTypeface(typeface: Typeface?) {
        super.setTypeface(typeface)
        tEdit.typeface = typeface
    }

    override fun customSetTypeface(typeface: Typeface?, color: Int) {
        this.typeface = typeface
        setTextColor(color)
    }

    override fun setFocusable(focusable: Boolean) {
        tEdit.isFocusable = focusable
    }

    override fun setGravity(value: Int) {
        tEdit.gravity = value
    }

    override fun setCounterMaxLength(maxLength: Int) {
        enabledTextError(maxLength > 0)
        super.setCounterMaxLength(maxLength)
    }

    fun setTextColor(color: Int = DEFAULT_VALUE_TEXT_COLOR) {
        tEdit.setTextColor(color)
    }

    fun setHintTextColor(color: Int = Color.LTGRAY) {
        tEdit.setHintTextColor(color)
    }

    fun setHintTextAppearance() {
        super.setHintTextAppearance(R.style.HintTextAppearance)
    }

    fun setLinesForMultilineText(
        lines: Int = DEFAULT_VALUE_LINES,
        maxLines: Int = DEFAULT_VALUE_MAX_LINE
    ) {
        if (customType != TypeCustomInputText.TEXT_MULTILINE) return
        var dataLine = lines
        if (lines > maxLines) dataLine = maxLines
        tEdit.setLines(dataLine)
        tEdit.maxLines = maxLines
    }

    override fun setEnabled(enabled: Boolean) {
        try {
            tEdit.isEnabled = when (customType) {
                TypeCustomInputText.READ_ONLY_TEXT, TypeCustomInputText.READ_ONLY_MULTILINE -> {
                    tEdit.setTextColor(Color.LTGRAY)
                    if (customType == TypeCustomInputText.READ_ONLY_TEXT)
                        tEdit.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                    tEdit.isClickable = false
                    tEdit.isFocusable = false
                    false
                }
                else -> enabled
            }
        } catch (e: Exception) {
        } finally {
            super.setEnabled(enabled)
        }
    }

    fun setReadOnlyText(
        text: String,
        @IntRange(from = 8, to = 50) size: Int = 32,
        textColor: Int = Color.DKGRAY,
        backgroundColor: Int = Color.WHITE,
        rules: Array<Int>? = null
    ) {
        if (text.isEmpty()) return
        this.customType =
            if (text.contains('\n')) TypeCustomInputText.READ_ONLY_MULTILINE else TypeCustomInputText.READ_ONLY_TEXT
        layoutParams = this.getCustomLayoutRelativeLayout().apply {
            height = RelativeLayout.LayoutParams.WRAP_CONTENT
            width = RelativeLayout.LayoutParams.MATCH_PARENT
            if (rules != null && rules.any()) {
                rules.forEach { r ->
                    try {
                        addRule(r)
                    } catch (ex: Exception) {
                    }
                }
            }
        }
        this.tEdit.clearComposingText()
        this.text = text
        textSize = size.toFloat()
        setTextColor(textColor)
        setBackgroundColor(backgroundColor)
        gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        isClickable = false
        isFocusable = false
        inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        boxBackgroundMode = BOX_BACKGROUND_NONE
    }

    override fun toString(): String = text
    //endregion

    //region functional
    public fun setOnlyTextUpper() {
        if (this.typeCustomType != TypeCustomInputTextType.TEXT) return
        this.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    }
//    public fun setOnlyTextLower(){
//        if(this.typeCustomType != TypeCustomInputTextType.TEXT) return
//        this.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
//    }

    private fun makeWatches(): TextWatcher {
        return setTextWatcher(after = { s, e ->
            if (!e) this.error = null
            else {
                if (s!!.length > this.counterMaxLength)
                    this.error = "El número máximo de caracteres es: ${this.counterMaxLength}"
                else error = null
            }
        })
    }

    private fun customSetPadding(value: Int) {
        try {
            val array = customArrayParamLayout(value)
            tEdit.layoutParams = tEdit.setEditCustomLayoutRelativeLayout().apply {
                setPadding(array[0], array[1], array[2], array[3])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun customSetMargin(value: Int) {
        try {
            val array = customArrayParamLayout(value)
            tEdit.layoutParams = tEdit.setEditCustomLayoutRelativeLayout().apply {
                setMargins(array[0], array[1], array[2], array[3])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun customArrayParamLayout(value: Int): ArrayList<Int> {
        val m1 = value.toPixel(ctx)
        val m2 = floor(value.toDouble() / 2).toInt().toPixel(ctx)
        return arrayListOf(m1, m2, m1, m2)
    }

    private fun enabledTextError(value: Boolean = false) {
        if (value) {
            try {
                tEdit.removeTextChangedListener(textWatcher)
            } finally {
                tEdit.addTextChangedListener(textWatcher)
            }
        } else {
            try {
                tEdit.removeTextChangedListener(textWatcher)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setDatePicker(activity: AppCompatActivity, action: (() -> Unit)? = null) {
        this.tEdit.isFocusable = false
        this.tEdit.isClickable = true
        this.tEdit.setOnClickListener {
            val date = this.date
            val newFragment = DatePickerFragment.newInstance(date) {
                this.text = it.toDateString(this.customFormatDate)
            }
            newFragment.show(activity.supportFragmentManager, "datePicker")
        }
        if (action != null) this.tEdit.addTextWatcher(after = { _, e -> if (!e) action.invoke() })
    }

    fun setMask(type: MaskType, setHint: Boolean = false) {
        val listener: MaskedTextChangedListener = MaskedTextChangedListener.installOn(
            this.tEdit,
            type.mask,
            listOf(type.mask),
            AffinityCalculationStrategy.PREFIX,
        )
        if (setHint) {
            try {
                this.hint = listener.placeholder()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        this.tEdit.text = null
        this.tEdit.inputType = type.type
        this.tEdit.keyListener = DigitsKeyListener.getInstance(type.digits)
    }

    private fun toDate(): Date? = when (this.text.isNotEmpty()) {
        true -> this.text.toDate()
        false -> null
    }

    fun toDate(format: FormatDateString): Date? =
        toDate().toDateString(format).toDate(format)

    fun toInt(): Int = when (this.text.isEmpty()) {
        true -> 0
        false -> this.text.toInt()
    }

    fun toLong(): Long = when (this.text.isEmpty()) {
        true -> 0L
        false -> this.text.toLong()
    }

    fun toFloat(): Float = when (this.text.isEmpty()) {
        true -> 0F
        false -> this.text.toFloat()
    }

    fun toDouble(): Double = when (this.text.isEmpty()) {
        true -> 0.0
        false -> this.text.toDouble()
    }
    //endregion

    companion object {
        private const val TEXT_OF_TEST: String = "La puerca esta en la cocina..."
        private const val TEXT_OF_TEST_HINT: String = "Texto de prueba"

        private const val DEFAULT_VALUE_DECIMAL_PLACE: Int = 2
        private const val DEFAULT_VALUE_IS_MULTILINE: Boolean = false
        private const val DEFAULT_VALUE_MAX_LENGTH: Int = 0
        private val DEFAULT_VALUE_DATE_FORMAT_STRING: FormatDateString = FormatDateString.SIMPLE
        private const val DEFAULT_VALUE_TEXT_COLOR: Int = Color.BLACK
        private val DEFAULT_VALUE_TYPE: TypeCustomInputText = TypeCustomInputText.TEXT
        private const val DEFAULT_VALUE_LINES = 15
        private const val DEFAULT_VALUE_MAX_LINE = 100
        private const val DEFAULT_VALUE_PADDING = 10
        private const val DEFAULT_VALUE_MARGIN = 5
        private const val DEFAULT_STROKE_WIDTH = 5
    }
}