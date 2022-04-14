package com.vsg.helper.ui.widget.spinner

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IntRange
import com.vsg.helper.R
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.getItem
import com.vsg.helper.helper.HelperUI.Static.getItemEnum
import com.vsg.helper.helper.HelperUI.Static.getItemEnumOrDefault
import com.vsg.helper.helper.HelperUI.Static.setCustomAdapter
import com.vsg.helper.helper.HelperUI.Static.setCustomAdapterEnum
import com.vsg.helper.helper.HelperUI.Static.setEditCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.setItem
import com.vsg.helper.helper.HelperUI.Static.setItemEnum
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.helper.type.TypeMakeLayoutParameter
import com.vsg.helper.ui.adapter.*
import com.vsg.helper.ui.util.BaseActivity

class CustomSpinner @JvmOverloads constructor(
    private val ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRest: Int = 0,
    addId: Boolean = false,
    theme: CustomTheme = CustomTheme.WHITE
) : RelativeLayout(ctx, attrs, defStyleAttr, defStyleRest) {

    //region private properties
    private var tTitle: TextView
    private var tSpinner: Spinner
    private var tContainer: RelativeLayout
    private val shape: GradientDrawable
        get() {
            return GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(customBackColor, customBackColor)
            ).apply {
                this.cornerRadii = (1..8).map { 4F }.toFloatArray()
                setStroke(
                    customStroke.toPixel(context),
                    customColorStroke
                )
            }
        }
    //endregion

    //region properties
    @IntRange(from = 0, to = 10)
    var customStroke: Int = DEFAULT_CUSTOM_STOKE
    var customBackColor: Int = Color.WHITE
        set(value) {
            field = value
            this.tTitle.setBackgroundColor(value)
            this.setBackgroundColor(value)
            this.tSpinner.setBackgroundColor(value)
            this.tSpinner.setPopupBackgroundDrawable(ColorDrawable(value))
        }
    var customTextColor: Int = Color.BLACK
        set(value) {
            field = value
            this.tTitle.setTextColor(value)
        }
    var customTextSize: Float = DEFAULT_TEXT_SIZE
        set(value) {
            field = value
            this.tTitle.textSize = value
        }
    var customColorStroke: Int = Color.BLACK
        set(value) {
            field = value
            this.tContainer.background = shape
        }
    var customTitle: String
        get() = tTitle.text.toString()
        set(value) {
            tTitle.text = when (value.isEmpty()) {
                true -> ""
                false -> " ${value}:  "
            }
        }
    var customMargin: Int = DEFAULT_MARGIN
        get() = field.toPixel(ctx)
        set(value) {
            field = value
            this.setEditCustomLayoutRelativeLayout().apply {
                setMargins(value, value, value, value)
            }
        }
    var customMarginSpinner: Int = DEFAULT_MARGIN_SPINNER
        get() = field.toPixel(ctx)
    var customHeight: Int = DEFAULT_HEIGHT
        get() = field.toPixel(ctx)
        set(value) {
            field = when(value < DEFAULT_MINIMUM_HEIGHT){
                true -> DEFAULT_MINIMUM_HEIGHT
                false -> value
            }
            this.tSpinner.setEditCustomLayoutRelativeLayout().apply {
                height = field.toPixel(ctx)
            }
        }
    var customTheme: CustomTheme = CustomTheme.WHITE
        set(value) {
            field = value
            when(value){
                CustomTheme.WHITE -> {
                    customTextColor = Color.BLACK
                    customBackColor = Color.WHITE
                    customColorStroke = Color.BLACK
                }
                CustomTheme.BLACK -> {
                    customTextColor = Color.WHITE
                    customBackColor = Color.BLACK
                    customColorStroke = Color.WHITE
                }
            }
        }
    //endregion

    //region adapter
    var adapterType: AdapterType = AdapterType.UNDEFINED
        private set
    var adapter: SpinnerAdapter?
        get() = tSpinner.adapter
        set(value) {
            tSpinner.adapter = value
        }

    //endregion

    //region fields
    private val marginSpinnerTop: Int
        get() = DEFAULT_MARGIN_SPINNER_TOP.toPixel(ctx)
    private val marginContainer: Int
        get() = DEFAULT_MARGIN_CONTAINER.toPixel(ctx)
    private var marginTopStoke: Int = DEFAULT_MARGIN_TOP_STOKE
        get() = field.toPixel(ctx)
        set(value) {
            field = value
            tContainer.setEditCustomLayoutRelativeLayout().apply {
                topMargin = value
            }
        }

    //endregion

    //region init

    init {
        if(addId) this.id = ViewGroup.generateViewId()

        tSpinner = Spinner(context).apply {
            layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout()
                    .apply {
                        setMargins(customMarginSpinner, marginSpinnerTop, customMarginSpinner, 5)
                        gravity = Gravity.CENTER_VERTICAL
                        height = customHeight
                        width = ViewGroup.LayoutParams.MATCH_PARENT
                        addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    }
            gravity = Gravity.CENTER_VERTICAL
//            setPopupBackgroundDrawable(ColorDrawable(customBackColor))

//            this.setBackgroundColor(Color.BLUE)

//            val adapter = ArrayAdapter(ctx, R.layout.list_item_simple_adapter, dataTest)
//            this.adapter = adapter
        }
        tTitle = TextView(ctx).apply {
            textSize = customTextSize
            setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD_ITALIC)
//            setBackgroundColor(customBackColor)
//            setTextColor(this@CustomSpinner.customTextColor)
            layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout(TypeMakeLayoutParameter.WIDTH_WRAP_HEIGHT_WRAP)
                    .apply {
                        setMargins(35, -15, 0, 0)
                    }
        }
        tContainer = RelativeLayout(ctx).apply {
            this.background = shape
            this.layoutParams =
                HelperUI.makeCustomLayoutRelativeLayout()
                    .apply {
                        setMargins(
                            marginContainer,
                            marginTopStoke,
                            marginContainer,
                            marginContainer
                        )
                    }
            this.addView(tSpinner)

        }
        this.addView(tContainer)
        this.addView(tTitle)
        this.layoutParams =
            HelperUI.makeCustomLayoutRelativeLayout()
                .apply {
                    height = tSpinner.layoutParams.height + 60
                    width = tSpinner.layoutParams.width
                    setMargins(customMargin, customMargin, customMargin, customMargin)
                }


        customTheme = theme
        tSpinner.apply {
            setPopupBackgroundDrawable(ColorDrawable(customBackColor))
        }
        tTitle.apply {
            setBackgroundColor(customBackColor)
            setTextColor(customTextColor)
        }
        setBackgroundColor(customBackColor)

        setupAttributes(attrs)
//        this.customTitle = TEXT_OF_TEST

//        this.tSpinner.setBackgroundColor(Color.BLUE)
        //adapter for test
//        var index: Long = 0
//        val dataTest: List<AdapterTitleTest> = (1..5)
//            .map { "temp ${it.toPadStart(3)}" }
//            .associateBy { index++ }
//            .map { it -> AdapterTitleTest(it.value, it.key) }
//        this.adapter = UIDataAdapterGenericEntity(ctx, dataTest, true)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.CustomSpinner,
            0, 0
        )
        // Extract custom attributes into member variables
        customStroke =
            typedArray.getInt(
                R.styleable.CustomSpinner_customSpinnerStroke,
                DEFAULT_CUSTOM_STOKE
            )
        customBackColor =
            typedArray.getInt(
                R.styleable.CustomSpinner_customSpinnerBackColor,
                Color.WHITE
            )
        customTextColor =
            typedArray.getColor(
                R.styleable.CustomSpinner_customSpinnerTextColor,
                Color.BLACK
            )
        customTextSize =
            typedArray.getFloat(
                R.styleable.CustomSpinner_customSpinnerTextSize,
                DEFAULT_TEXT_SIZE
            )
        customColorStroke =
            typedArray.getInt(
                R.styleable.CustomSpinner_customSpinnerColorStroke,
                Color.BLACK
            )
        customTitle =
            typedArray.getString(
                R.styleable.CustomSpinner_customSpinnerTitle
            ) ?: ""
        customMarginSpinner =
            typedArray.getInt(
                R.styleable.CustomSpinner_customSpinnerMarginSpinner,
                DEFAULT_MARGIN_SPINNER
            )
        customMargin =
            typedArray.getInt(
                R.styleable.CustomSpinner_customSpinnerMargin,
                DEFAULT_MARGIN
            )
        customHeight =
            typedArray.getInt(
                R.styleable.CustomSpinner_customSpinnerHeight,
                DEFAULT_HEIGHT
            )
        marginTopStoke =
            typedArray.getInt(
                R.styleable.CustomSpinner_customSpinnerMarginTopStoke,
                DEFAULT_MARGIN_TOP_STOKE
            )
        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }
    //endregion

    //region function
    fun <T> setCustomAdapterEnum(
        type: Class<T>,
        callBackItemSelect: ((T?) -> Unit)? = null,
        callBackSetTextView: ((TextView) -> Unit)? = { t -> t.setTextColor(customTextColor) },
        selectItem: T? = null
    ): UIDataAdapterGenericEnum<T>?
            where T : IDataAdapterEnum,
                  T : Enum<T> {
        this.adapterType = AdapterType.UNDEFINED
        if (ctx !is BaseActivity) return null
        var temp: UIDataAdapterGenericEnum<T>? = null
        try {
            temp = this.tSpinner.setCustomAdapterEnum(
                ctx,
                type,
                callBackItemSelect,
                callBackSetTextView,
                selectItem
            )
            this.adapterType = AdapterType.ENUM
        } catch (ex: Exception) {
            this.adapterType = AdapterType.UNDEFINED
        }
        return temp
    }

    fun <T> setCustomAdapter(
        data: List<T>,
        callBackItemSelect: ((T?) -> Unit)? = null,
        callBackSetTextView: ((TextView) -> Unit)? = { t -> t.setTextColor(customTextColor) },
        selectItem: T? = null,
        customTextSize: Int = resources.getInteger(R.integer.CustomSpinnerTitleTextSize)
    ): UIDataAdapterGenericEntity<T>? where T : IDataAdapterTitle, T : IEntity {
        if (ctx !is BaseActivity) return null
        var temp: UIDataAdapterGenericEntity<T>? = null
        try {
            temp = this.tSpinner.setCustomAdapter(
                ctx,
                data,
                callBackItemSelect,
                callBackSetTextView,
                selectItem
            )
            this.customTextSize = customTextSize.toFloat()
            this.adapterType = AdapterType.ENTITY
        } catch (ex: Exception) {
            this.adapterType = AdapterType.UNDEFINED
        }
        return temp
    }

    fun getAdapter(): UIDataAdapterGenericBase<*>? {
        val temp = when (adapterType) {
            AdapterType.UNDEFINED -> null
            AdapterType.ENUM -> when (this.adapter is UIDataAdapterGenericEnum<*>) {
                true -> this.adapter as UIDataAdapterGenericEnum<*>
                false -> null
            }
            AdapterType.ENTITY -> when (this.adapter is UIDataAdapterGenericEntity<*>) {
                true -> this.adapter as UIDataAdapterGenericEntity<*>
                false -> null
            }
        }
        return temp
    }

    fun <T> addCallBackForItemSelect(callBackItemSelect: ((T?) -> Unit)? = null) where T : IDataAdapterTitle {
        this.tSpinner.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    try {
                        val temp: T? =
                            (this@apply.adapter as UIDataAdapterGenericBase<*>).getItem(
                                position
                            ) as T?
                        callBackItemSelect?.invoke(temp)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    fun removeCallBackForItemSelect() {
        this.tSpinner.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }

    fun <T> setItem(
        item: T,
        action: (() -> Unit)? = null
    ) where T : IDataAdapterTitle, T : IEntity {
        this.tSpinner.setItem(item, action)
    }

    fun <T> setItem(
        id: Int,
        action: (() -> Unit)? = null
    ) where T : IDataAdapterTitle, T : IEntity {
        this.tSpinner.setItem<T>(id, action)
    }

    fun <T> setItemEnum(item: T) where T : IDataAdapterEnum, T : Enum<T> {
        tSpinner.setItemEnum(item)
    }

    fun <T> getItem(): T? where T : IDataAdapterTitle, T : IEntity {
        return tSpinner.getItem()

    }

    fun <T> getItemEnumOrDefault(): T? where T : IDataAdapterEnum, T : Enum<T> {
        return tSpinner.getItemEnumOrDefault()
    }

    fun <T> getItemEnum(): T? where T : IDataAdapterEnum, T : Enum<T> {
        return tSpinner.getItemEnum()
    }
    //endregion

    //region override
    fun setTypeface(typeface: Typeface?) {
        tTitle.typeface = typeface
    }

    override fun setFocusable(focusable: Boolean) {
        tSpinner.isFocusable = focusable
    }

    override fun setGravity(value: Int) {
        tSpinner.gravity = value
    }
    //endregion

    //region enum
    enum class AdapterType {
        ENTITY,
        ENUM,
        UNDEFINED
    }
    enum class CustomTheme{
        WHITE,
        BLACK
    }

    //endregion

    companion object {
        private const val TEXT_OF_TEST: String = "Código de país"

        private const val DEFAULT_MARGIN_SPINNER: Int = 10
        private const val DEFAULT_MARGIN_SPINNER_TOP: Int = 10
        private const val DEFAULT_MARGIN_CONTAINER: Int = 1
        private const val DEFAULT_MARGIN_TOP_STOKE: Int = 25
        private const val DEFAULT_HEIGHT: Int = 35
        private const val DEFAULT_MINIMUM_HEIGHT: Int = 30
        private const val DEFAULT_CUSTOM_STOKE: Int = 1
        private const val DEFAULT_TEXT_SIZE: Float = 20F
        private const val DEFAULT_MARGIN: Int = 10
    }
}