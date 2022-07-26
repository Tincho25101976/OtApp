package com.vsg.helper.helper

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import com.vsg.helper.R
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.util.viewModel.IViewModelUpdateSetEnabled
import com.vsg.helper.helper.HelperEnum.Companion.getDefaultEnum
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.date.HelperDate.Companion.formatDate
import com.vsg.helper.helper.date.HelperDate.Companion.toDate
import com.vsg.helper.helper.date.HelperDate.Companion.toShortDateString
import com.vsg.helper.helper.mask.MaskType
import com.vsg.helper.helper.type.TypeMakeLayoutParameter
import com.vsg.helper.ui.adapter.*
import com.vsg.helper.ui.adapter.paging.UIRecyclerAdapterPagingData
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.datePicker.DatePickerFragment
import com.vsg.helper.ui.widget.text.CustomEditText
import java.io.File
import java.util.*
import kotlin.math.floor

class HelperUI {
    companion object Static {
        const val REQUEST_FOR_PERMISSION_STORAGE = 1000
        const val REQUEST_FOR_PERMISSION_CAMERA = 1001
        const val REQUEST_FOR_PERMISSION_INTERNET = 1002
        const val REQUEST_FOR_PERMISSION_ACCESS_NETWORK_STATE = 1003
        const val REQUEST_FOR_PERMISSION_READ_STATE_PHONE = 1004
        const val REQUEST_FOR_PERMISSION_READ_PHONE_NUMBER = 1005

        const val REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER = 100
        const val REQUEST_FOR_IMAGE_CAPTURE = 101
        const val REQUEST_FOR_TAKE_SCREENSHOT = 102
        const val REQUEST_FOR_TAKE_PHOTO = 103
        const val REQUEST_FOR_SEND_PICTURE = 104

        const val DS_PHOTO_EDITOR_REQUEST_CODE = 200

        const val DEFAULT_TEXT_SIZE_FOR_SPINNER = 20

        //region textView
        fun EditText.isEmpty(): Boolean = this.text.isEmpty()
        fun EditText.isChecked(group: String): Boolean {
            if (!this.isEmpty()) return true
            val sb = StringBuilder().append("Complete el campo ").append(this.tooltipText)
            val str = when (group.isEmpty()) {
                true -> ""
                false -> "( $group )"
            }
            throw Exception(sb.append(str).toString())
        }

        fun TextView.isChecked(group: String): Boolean {
            if (this.text.isNotEmpty()) return true
            val sb = StringBuilder().append("Complete el campo ").append(this.tooltipText)
            val str = when (group.isEmpty()) {
                true -> ""
                false -> "( $group )"
            }
            throw Exception(sb.append(str).toString())
        }

        //region mask
        fun EditText.setMask(type: MaskType, setHint: Boolean = true) {
            val listener: MaskedTextChangedListener = MaskedTextChangedListener.installOn(
                this,
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
            this.text = null
            this.inputType = type.type
            this.keyListener = DigitsKeyListener.getInstance(type.digits)
        }
        //endregion

        fun EditText.toText(): String = this.text.toString()
        fun TextView.toText(): String = this.text.toString()
        fun TextView.toInt(): Int = when (this.text.isNullOrEmpty()) {
            true -> 0
            false -> this.text.toString().toInt()
        }

        fun TextView.toDouble(): Double = when (this.text.isNullOrEmpty()) {
            true -> 0.0
            false -> this.text.toString().toDouble()
        }

        fun EditText.toDate(): Date? =
            when (!this.text.isNullOrEmpty()) {
                true -> this.text.toString().toDate()
                false -> null
            }


        fun EditText.toDate(format: FormatDateString): Date? =
            when (!this.text.isNullOrEmpty()) {
                true -> this.text.toString().toDate(format)
                false -> null
            }

        fun EditText.setDatePicker(activity: AppCompatActivity, action: (() -> Unit)? = null) {
            this.isFocusable = false
            this.isClickable = true
            this.setOnClickListener {
                val date = this.toDate()
                val newFragment = DatePickerFragment.newInstance(date) {
                    this.text = it.toShortDateString().toEditable()
                }
                newFragment.show(activity.supportFragmentManager, "datePicker")
            }
            if (action != null) this.addTextWatcher(after = { _, e -> if (!e) action.invoke() })
        }

        fun CustomEditText.setDatePicker(
            activity: AppCompatActivity,
            action: (() -> Unit)? = null
        ) {
            this.isFocusable = false
            this.isClickable = true
            this.setOnClickListener {
                val date = this.toDate()
                val newFragment = DatePickerFragment.newInstance(date) {
                    this.text = it.toShortDateString().toEditable()
                }
                newFragment.show(activity.supportFragmentManager, "datePicker")
            }
            if (action != null) this.addTextWatcher(after = { _, e -> if (!e) action.invoke() })
        }

        private fun getTextWatcher(imageView: ImageView): TextWatcher {
            return object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    imageView.setStatus(!s.isNullOrEmpty())
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    imageView.setStatus(!s.isNullOrEmpty())
                }

                override fun afterTextChanged(s: Editable?) {
                    imageView.setStatus(!s.isNullOrEmpty())
                }
            }
        }

        fun TextView.addTextWatcher(
            before: ((CharSequence?, Boolean) -> Unit)? = null,
            onText: ((CharSequence?, Boolean) -> Unit)? = null,
            after: ((Editable?, Boolean) -> Unit)? = null
        ) {
            this.removeTextChangedListener(setTextWatcher(before, onText, after))
            this.addTextChangedListener(setTextWatcher(before, onText, after))
        }

        fun CustomEditText.addTextWatcher(
            before: ((CharSequence?, Boolean) -> Unit)? = null,
            onText: ((CharSequence?, Boolean) -> Unit)? = null,
            after: ((Editable?, Boolean) -> Unit)? = null
        ) {
            this.removeTextChangedListener(setTextWatcher(before, onText, after))
            this.addTextChangedListener(setTextWatcher(before, onText, after))
        }

        fun setTextWatcher(
            before: ((CharSequence?, Boolean) -> Unit)? = null,
            onText: ((CharSequence?, Boolean) -> Unit)? = null,
            after: ((Editable?, Boolean) -> Unit)? = null
        ): TextWatcher {
            return object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    before?.invoke(s, !s.isNullOrEmpty())
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    onText?.invoke(s, !s.isNullOrEmpty())
                }

                override fun afterTextChanged(s: Editable?) {
                    after?.invoke(s, !s.isNullOrEmpty())
                }
            }
        }

        fun <T> AutoCompleteTextView.setCustomAdapter(
            context: T,
            adapter: List<String>?,
            @IntRange(from = 10, to = 48)
            textSize: Int = resources.getInteger(R.integer.CustomAdapterTextSize),
            ignoreCase: Boolean = false,
            callbackOnItemClick: ((T, String) -> Unit)? = null,
            callbackOnKeyPressEnter: ((T, String) -> Unit)? = null,
            commandImageView: ImageView? = null
        ) where T : BaseActivity {
            val minTextSize = when (textSize >= 3) {
                true -> textSize - 2
                false -> 2
            }
            this.maxLines = 1
            this.setSingleLine()
            if (adapter != null && adapter.any()) {
                val customAdapter = UIStringDataAdapter(context, adapter, ignoreCase).apply {
                    onEventSetTextView = {
                        it.textSize = minTextSize.toFloat()
                    }
                }
                this.setAdapter(customAdapter)
                this.threshold = 1
                this.maxLines = 1
                this.setSingleLine()
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
                this.setDropDownBackgroundDrawable(ColorDrawable(Color.WHITE))

                this.setAutoSizeTextTypeUniformWithConfiguration(
                    minTextSize,
                    textSize + 2,
                    4,
                    TypedValue.COMPLEX_UNIT_SP
                )

                this.setOnItemClickListener { parent, _, position, _ ->
                    val selected = parent.adapter.getItem(position).toString()
                    text = selected.toEditable()
                    if (selected.isNotEmpty()) {
                        this.setSelection(text.length)
                        this.setFocus(true)
                        callbackOnItemClick?.invoke(context, selected)
                    }
                }
            }
            this.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    callbackOnKeyPressEnter?.invoke(context, this.toText())
                    this.dismissDropDown()
                    this.hideKeyboard()
                    return@setOnKeyListener true
                }
                false
            }
            if (commandImageView != null) {
                this.removeTextChangedListener(getTextWatcher(commandImageView))
                this.addTextChangedListener(getTextWatcher(commandImageView))
            }

        }
        //endregion

        //region view
        private fun View.showKeyboard() {
            this.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }

        private fun View.hideKeyboard() {
            this.requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }

        fun View.setFocus(hideKeyBoard: Boolean = true) {
            this.requestFocus()
            if (hideKeyBoard) this.hideKeyboard()
            else this.showKeyboard()
        }

        fun View.getCustomLayoutRelativeLayout(rule: Int = 0): RelativeLayout.LayoutParams {
            var layout: RelativeLayout.LayoutParams =
                this.layoutParams as RelativeLayout.LayoutParams
            if (rule > 0) layout.addRule(rule)
            layout.height = ViewGroup.LayoutParams.WRAP_CONTENT
            layout.width = ViewGroup.LayoutParams.MATCH_PARENT
            return layout
        }


        fun View.setEditCustomLayoutRelativeLayout(): RelativeLayout.LayoutParams =
            this.layoutParams as RelativeLayout.LayoutParams

        fun View.setEditCustomLayoutLinealLayout(): LinearLayout.LayoutParams =
            this.layoutParams as LinearLayout.LayoutParams


        fun View.getViewHeight(): Int {
            val wm: WindowManager =
                this.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val deviceWidth: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                val size: Point = Point()
                display.getSize(size)
                deviceWidth = size.x
            } else {
                deviceWidth = display.width
            }
            val widthMeasureSpec: Int =
                View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST)
            val heightMeasureSpec: Int =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            this.measure(widthMeasureSpec, heightMeasureSpec)
            return this.measuredHeight
        }
        //endregion

        //region layout
        private fun getSizeLayout(
            type: TypeMakeLayoutParameter = TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_WRAP,
            customWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
            customHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        ): Pair<Int, Int> {
            val width: Int
            val height: Int
            when (type) {
                TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_MATCH -> {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                TypeMakeLayoutParameter.WIDTH_WRAP_HEIGHT_WRAP -> {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_WRAP -> {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                TypeMakeLayoutParameter.WIDTH_WRAP_HEIGHT_MATCH -> {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                TypeMakeLayoutParameter.CUSTOM -> {
                    width = customWidth
                    height = customHeight
                }
            }
            return Pair(width, height)
        }

        fun makeCustomLayoutRelativeLayout(
            type: TypeMakeLayoutParameter = TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_WRAP,
            customWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
            customHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        ): RelativeLayout.LayoutParams {
            val size = getSizeLayout(type, customWidth, customHeight)
            return RelativeLayout.LayoutParams(size.first, size.second)
        }

        fun makeCustomLayoutLinealLayout(
            type: TypeMakeLayoutParameter = TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_WRAP,
            customWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
            customHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        ): LinearLayout.LayoutParams {
            val size = getSizeLayout(type, customWidth, customHeight)
            return LinearLayout.LayoutParams(size.first, size.second)
        }

        fun makeCustomLayoutGridLayout(
            type: TypeMakeLayoutParameter = TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_WRAP,
            customWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
            customHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        ): GridLayout.LayoutParams {
            val size = getSizeLayout(type, customWidth, customHeight)
            val data: GridLayout.LayoutParams = GridLayout.LayoutParams()
            data.height = size.second
            data.width = size.first
            return data
        }

        fun makeCustomLayoutFrameLayout(
            type: TypeMakeLayoutParameter = TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_WRAP,
            customWidth: Int = ViewGroup.LayoutParams.MATCH_PARENT,
            customHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        ): FrameLayout.LayoutParams {
            val size = getSizeLayout(type, customWidth, customHeight)
            return FrameLayout.LayoutParams(size.first, size.second)
        }
        //endregion

        //region imageView
        fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val bgDrawable = view.background
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)
            return bitmap
        }

        fun ImageView.getCropVisibility(): Bitmap? {
            val temp = takeScreenshotOfView(this, this.height, this.width)
            return temp
        }

        fun ImageView.getBitmap(): Bitmap? {
            val drawable = this.drawable
            return (drawable as BitmapDrawable).bitmap
        }

        fun ImageView.getArray(allowNull: Boolean = false): ByteArray? {
            val bitmap = this.getBitmap()
            return when (bitmap == null || bitmap.byteCount <= 0) {
                true -> when (allowNull) {
                    true -> null
                    false -> byteArrayOf()
                }
                false -> bitmap.toArray()
            }
        }

        fun Drawable.getBitmap(): Bitmap? {
            return (this as BitmapDrawable).bitmap
        }

        fun ImageView.setStatus(data: Boolean, alpha: UByte = 75u) {
            this.isEnabled = data
            this.imageAlpha = when (data) {
                true -> 255
                false -> alpha.toInt()
            }
        }

        fun ImageView.setStatusWhitWatcher(control: TextView) {
            control.removeTextChangedListener(getTextWatcher(this))
            control.addTextChangedListener(getTextWatcher(this))
        }

        fun ImageView.resize(height: Int = layoutParams.height, width: Int = layoutParams.width) {
            layoutParams.apply {
                this.height = height
                this.width = width
                layoutParams = this
            }
        }

        fun ImageView.resize(size: Int) = this.resize(size, size)
        fun ImageView.setScaleBitmap(bitmap: Bitmap): Bitmap? {
            val currentBitmapWidth: Int = bitmap.width
            val currentBitmapHeight: Int = bitmap.height
            val newWidth: Int = this.width
            val newHeight =
                floor(currentBitmapHeight.toDouble() * (newWidth.toDouble() / currentBitmapWidth.toDouble()))
                    .toInt()

            var data: Bitmap?
            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true).also {
                data = it.copy(Bitmap.Config.ARGB_8888, true)
            }
            return data
        }

        fun ImageView.setPictureFromBitmap(bitmap: Bitmap): Bitmap? {
            val currentBitmapWidth: Int = bitmap.width
            val currentBitmapHeight: Int = bitmap.height
            val newWidth: Int = this.width
            val newHeight =
                floor(currentBitmapHeight.toDouble() * (newWidth.toDouble() / currentBitmapWidth.toDouble()))
                    .toInt()

            var data: Bitmap?
            try {
                Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true).also {
                    this.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    this.setImageBitmap(it)
                    this.adjustViewBounds = true
//                    this.layoutParams.apply {  }
                    data = it.copy(Bitmap.Config.ARGB_8888, true)
                }
            } catch (e: Exception) {
                data = null
            }
            return data
        }

        fun ImageView.setPictureFromFile(file: File) {
            val currentBitmap: Bitmap? = BitmapFactory.decodeFile(file.absolutePath)
            if (currentBitmap != null) this.setPictureFromBitmap(currentBitmap)
        }

        fun ImageView.setGrayScale(status: Boolean) {
            if (status) this.colorFilter = null
            else {
                val colorMatrix = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0F) })
                this.colorFilter = colorMatrix
            }
        }
        //endregion

        //region spinner
        fun <TActivity, T> Spinner.setCustomAdapterEnum(
            activity: TActivity,
            type: Class<T>,
            callBackItemSelect: ((T?) -> Unit)? = null,
            callBackSetTextView: ((TextView) -> Unit)? = null,
            selectItem: T? = null
        ): UIDataAdapterGenericEnum<T>
                where TActivity : BaseActivity,
                      T : IDataAdapterEnum, T : Enum<T> {

            val dataAdapter = UIDataAdapterGenericEnum(activity, type, true)
            this.setCustomAdapterBase(
                dataAdapter = dataAdapter,
                callBackItemSelect = callBackItemSelect,
                callBackSetTextView = callBackSetTextView,
                selectItem = selectItem,
                textSize = resources.getInteger(R.integer.CustomSpinnerTextSize)
            )
            return dataAdapter
        }

        fun <TActivity, T> Spinner.setCustomAdapter(
            activity: TActivity,
            data: List<T>,
            callBackItemSelect: ((T?) -> Unit)? = null,
            callBackSetTextView: ((TextView) -> Unit)? = null,
            selectItem: T? = null
        ): UIDataAdapterGenericEntity<T>
                where TActivity : BaseActivity,
                      T : IDataAdapterTitle, T : IEntity {

            val dataAdapter = UIDataAdapterGenericEntity(activity, data, true)
            this.setCustomAdapterBase(
                dataAdapter = dataAdapter,
                callBackItemSelect = callBackItemSelect,
                callBackSetTextView = callBackSetTextView,
                selectItem = selectItem,
                textSize = resources.getInteger(R.integer.CustomSpinnerTextSize)
            )
            return dataAdapter
        }

        private fun <T> Spinner.setCustomAdapterBase(
            dataAdapter: UIDataAdapterGenericBase<T>,
            @IntRange(from = 10, to = 48)
            textSize: Int = 18,
            callBackItemSelect: ((T?) -> Unit)? = null,
            callBackSetTextView: ((TextView) -> Unit)? = null,
            selectItem: T? = null
        ): UIDataAdapterGenericBase<T>
                where T : IDataAdapterTitle {
            this.apply {
                adapter = dataAdapter.apply {
                    onSetTextView = { t ->
                        callBackSetTextView?.invoke(t)
                        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
                    }
                }
                setPopupBackgroundDrawable(ColorDrawable(Color.WHITE))

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        try {
                            val temp: T? =
                                (this@apply.adapter as UIDataAdapterGenericBase<T>).getItem(
                                    position
                                )
                            callBackItemSelect?.invoke(temp)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
                if (selectItem != null) {
                    try {
                        val position =
                            (this@apply.adapter as UIDataAdapterGenericBase<T>).getPosition(
                                selectItem
                            )
                        if (position >= 0) this.setSelection(position)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return dataAdapter
        }

        fun <T> Spinner.setItem(
            item: T,
            adapter: UIDataAdapterGenericEntity<T>
        ) where T : IDataAdapterTitle, T : IEntity {
            try {
                val position = adapter.getPosition(item)
                if (position >= 0) this.setSelection(position)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun <T> Spinner.setItem(
            item: T,
            action: (() -> Unit)? = null
        ) where T : IDataAdapterTitle, T : IEntity {
            try {
                val position = (this.adapter as UIDataAdapterGenericEntity<T>).getPosition(item)
                if (position >= 0) {
                    this.setSelection(position)
                    action?.invoke()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun <T> Spinner.setItemEnum(item: T) where T : IDataAdapterEnum, T : Enum<T> {
            try {
                val position = (this.adapter as UIDataAdapterGenericEnum<T>).getPosition(item)
                if (position >= 0) this.setSelection(position)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun <T> Spinner.setItem(
            id: Int,
            adapter: UIDataAdapterGenericEntity<T>
        ) where T : IDataAdapterTitle, T : IEntity {
            val item: T = adapter.dataSource.firstOrNull { it.id == id } ?: return
            this.setItem(item, adapter)
        }

        fun <T> Spinner.setItem(
            id: Int,
            action: (() -> Unit)? = null
        ) where T : IDataAdapterTitle, T : IEntity {
            if (this.adapter == null) return
            val item: T =
                (this.adapter as UIDataAdapterGenericEntity<T>).dataSource.firstOrNull { it.id == id }
                    ?: return
            this.setItem(item, action)
        }

        fun <T> Spinner.getItem(): T? where T : IDataAdapterTitle, T : IEntity {
            return (adapter as UIDataAdapterGenericEntity<T>).getItem(selectedItemPosition)

        }

        fun <T> Spinner.getItemEnumOrDefault(): T? where T : IDataAdapterEnum, T : Enum<T> {
            val ad: UIDataAdapterGenericEnum<T> = (adapter as UIDataAdapterGenericEnum<T>)
            val data: T? = ad.getItem(selectedItemPosition)
            return when (data == null) {
                true -> ad.type.getDefaultEnum()
                false -> data
            }
        }

        fun <T> Spinner.getItemEnum(): T? where T : IDataAdapterEnum, T : Enum<T> {
            return (adapter as UIDataAdapterGenericEnum<T>).getItem(selectedItemPosition)

        }
        //endregion

        //region RecyclerView
        fun <T> RecyclerView.initSwipeToEnabled(
            pagingAdapter: UIRecyclerAdapterPagingData<T>,
            @LayoutRes layoutMenu: Int? = null,
            viewModel: IViewModelUpdateSetEnabled<T>? = null
        ) where T : IResultRecyclerAdapter,
                T : IEntityPagingLayoutPosition,
                T : IEntity,
                T : Comparable<T> {
            ItemTouchHelper(object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    val direction: Int = when (viewModel == null) {
                        true -> ItemTouchHelper.LEFT
                        false -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    }
                    return makeMovementFlags(0, direction)
                }

                override fun onMove(
                    recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    (viewHolder as UIRecyclerAdapterPagingData<T>.UIRecyclerViewHolder).apply {
                        val index: Int = viewHolder.layoutPosition
                        this.item?.let {
                            when (direction) {
                                ItemTouchHelper.LEFT -> {
                                    pagingAdapter.showMenu(index)
                                    pagingAdapter.notifyItemChanged(index)
                                }
                                ItemTouchHelper.RIGHT -> {
                                    viewModel?.viewModelUpdateSetEnabled(it)
                                }
                                else -> Unit
                            }
                        }
                        pagingAdapter.notifyDataSetChanged()

                    }
                }

                override fun getAnimationDuration(
                    recyclerView: RecyclerView,
                    animationType: Int,
                    animateDx: Float,
                    animateDy: Float
                ): Long {
                    return when (animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG) {
                        true -> DEFAULT_DRAG_ANIMATION_DURATION.toLong()
                        else -> DEFAULT_SWIPE_ANIMATION_DURATION.toLong()
                    }
                }

            }).attachToRecyclerView(this)

            pagingAdapter.apply {
                onEventSetLayoutForMenu = {
                    layoutMenu
                }
            }
        }
        //endregion

        //region primitive
        fun List<String>.getArrayAdapter(context: Context): ArrayAdapter<String> {
            return ArrayAdapter<String>(context, R.layout.list_item_simple_adapter, this)
        }

        fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
        fun Float.toEditable(): Editable = this.toString().toEditable()
        fun Double.toEditable(): Editable = this.toString().toEditable()
        fun Int.toEditable(): Editable = this.toString().toEditable()
        fun Date?.toEditable(): Editable = when (this == null) {
            true -> "".toEditable()
            false -> formatDate().format(this).toEditable()
        }
        //endregion
    }
}