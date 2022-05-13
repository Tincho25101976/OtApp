package com.vsg.helper.ui.util

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TypefaceSpan
import android.view.*
import android.widget.*
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.textfield.TextInputEditText
import com.vsg.helper.R
import com.vsg.helper.common.model.IId
import com.vsg.helper.helper.HelperUI.Static.REQUEST_FOR_IMAGE_CAPTURE
import com.vsg.helper.helper.HelperUI.Static.REQUEST_FOR_TAKE_PHOTO
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutLinealLayout
import com.vsg.helper.helper.HelperUI.Static.toEditable
import com.vsg.helper.helper.date.HelperDate
import com.vsg.helper.helper.file.HelperFile.Static.getTempFileForCamera
import com.vsg.helper.helper.file.HelperFile.Static.getURI
import com.vsg.helper.helper.font.CustomTypefaceSpan
import com.vsg.helper.helper.font.FontManager
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.helper.permission.HelperPerminission.Static.checkedPermissionCamera
import com.vsg.helper.helper.permission.HelperPerminission.Static.checkedPermissionStorage
import com.vsg.helper.helper.type.TypeMakeLayoutParameter
import com.vsg.helper.ui.layout.MainItemsLayoutScroll
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.helper.ui.widget.text.type.TypeCustomInputText
import com.vsg.helper.ui.widget.text.type.TypeCustomInputTextType
import java.io.File
import kotlin.random.Random

abstract class BaseActivity(@LayoutRes val view: Int) : AppCompatActivity(), View.OnTouchListener {

    private var actionBar: ActionBar? = null

    //region handler
    var onEventExecuteActivityResult: ((Int, Int, Intent?) -> Unit)? = null
    var onEventExecuteActivityOnTouch: ((View?, event: MotionEvent?) -> Boolean)? = null
    var onEventExecuteSetViewModel: ((BaseActivity) -> Unit)? = null
    var onEventLoadBackground: ((ViewGroup, Int) -> Unit)? = null
    var onEventPathForTakePhoto: ((File?) -> Unit)? = null
    var onEventMakeActivityForSelector: ((MainItemsLayoutScroll) -> ScrollView)? = null
    var onEventAfterSetContentView: ((RelativeLayout) -> Unit)? = null
    //endregion

    //region properties
    public val typeface: Typeface
        get() = this.typeFaceCustom(Typeface.BOLD_ITALIC)
    private val root: ViewGroup
        get() = findViewById<View>(android.R.id.content) as ViewGroup
    //endregion

    //region activity
    protected abstract fun onExecuteCreate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
        onEventAfterSetContentView?.invoke(root.findViewById(R.id.ActivityPagingGeneric))
        FontManager(this).replaceFonts(findViewById(android.R.id.content))
        this.actionBar = this.supportActionBar
        checkPermission()
        onEventExecuteSetViewModel?.invoke(this)
        onExecuteCreate()

        val viewGroup = this.findViewById<ViewGroup>(android.R.id.content)
        val data: Int = mapOf(
            0 to R.drawable.pic_backgroung_0, 1 to R.drawable.pic_backgroung_1,
            2 to R.drawable.pic_backgroung_2, 3 to R.drawable.pic_backgroung_3,
            4 to R.drawable.pic_backgroung_4, 5 to R.drawable.pic_backgroung_5,
            6 to R.drawable.pic_backgroung_6, 7 to R.drawable.pic_backgroung_7,
            8 to R.drawable.pic_backgroung_8, 9 to R.drawable.pic_backgroung_9
        )[Random.nextInt(0, 9)] ?: R.drawable.pic_background_add_item
        onEventLoadBackground?.invoke(viewGroup, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (data == null) return
//        if (data.data == null && data.extras == null) return
        onEventExecuteActivityResult?.invoke(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return onEventExecuteActivityOnTouch?.invoke(v, event) ?: false
    }
    //endregion

    //region database
    protected fun getDatabaseName(): String = getString(R.string.dbName)
    //endregion

    //region launch
    fun loadActivity(data: Class<*>, id: Int = 0, result: Int = 0, extra: Int = 0) {
        val intent = Intent(this, data)
        if (id > 0) intent.putExtra(getString(R.string.MsgData), id)
        if (extra > 0) intent.putExtra(getString(R.string.MsgExtra), extra)
        when (result == 0) {
            true -> this.startActivity(intent)
            false -> this.startActivityForResult(intent, result)
        }
    }

    protected fun loadActivity(data: Class<*>, id: IId, result: Int = 0, extra: IId? = null) {
        loadActivity(
            data = data, id = id.id, result = result,
            extra = when (extra == null) {
                true -> 0
                false -> extra.id
            }
        )
    }
    //endregion

    //region capture
    fun takeImageWithCamera(formatCamera: Boolean = false): File? {
        var result: File? = null
        if (this.checkedPermissionCamera()) {
            val s = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (applicationContext.packageManager.hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_ANY)) {

                if (formatCamera) {
                    val fileCapture = this.getTempFileForCamera()
                    if (fileCapture != null) {
                        s.putExtra(MediaStore.EXTRA_OUTPUT, this.getURI(fileCapture))
                        val bundle = Bundle()
                        bundle.putString(getString(R.string.MsgData), fileCapture.absolutePath)
                        result = fileCapture
                        onEventPathForTakePhoto?.invoke(fileCapture)
                        this.startActivityForResult(s, REQUEST_FOR_TAKE_PHOTO, bundle)
                    }
                }
                if (!formatCamera) this.startActivityForResult(s, REQUEST_FOR_IMAGE_CAPTURE)
            }
//            if (s.resolveActivity(this.packageManager) != null) {
//                if (formatCamera) {
//                    val fileCapture = this.getTempFileForCamera()
//                    if (fileCapture != null) {
//                        s.putExtra(MediaStore.EXTRA_OUTPUT, this.getURI(fileCapture))
//                        val bundle = Bundle()
//                        bundle.putString(getString(R.string.MsgData), fileCapture.absolutePath)
//                        result = fileCapture
//                        onEventPathForTakePhoto?.invoke(fileCapture)
//                        this.startActivityForResult(s, REQUEST_FOR_TAKE_PHOTO, bundle)
//                    }
//                }
//                if (!formatCamera) this.startActivityForResult(s, REQUEST_FOR_IMAGE_CAPTURE)
//            }
        }
        return result
    }
    //endregion

    //region menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.items_menu, menu)
//        val items: MenuItem = menu?.findItem(R.id.action_bar_item_update_db) ?: return true
//        val command: Button = items.actionView as Button
        //FontManager(this).replaceFonts(menu as ViewGroup)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val menuTitle = menuItem.title.toString()
            val spannableString = SpannableString(menuTitle)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val typefaceSpan = TypefaceSpan(typeface)
                spannableString.setSpan(
                    typefaceSpan,
                    0,
                    menuTitle.length,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            } else {
                val customTypefaceSpan = CustomTypefaceSpan("custom_menu", typeface)
                spannableString.setSpan(
                    customTypefaceSpan,
                    0,
                    menuTitle.length,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
            menuItem.title = spannableString
        }
        return true
    }
    //endregion

    //region actionbar
    protected fun setSubtitle(value: String) =
        (value as CharSequence).also { actionBar!!.subtitle = it }

    protected fun setIcon(r: Int) = actionBar!!.setIcon(r)
    protected fun setLogo(r: Int) = actionBar!!.setLogo(r)
    public fun makeCustomActionbar(
        title: String?,
        size: Float = 24F,
        color: Int = Color.WHITE
    ) {
        val tv = TextView(applicationContext)
        tv.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        tv.text = title
        tv.textSize = size
        tv.setTextColor(color)
        tv.typeface = this.typeface
        actionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.DKGRAY))
        actionBar!!.customView = tv
    }
    //endregion

    //region permission
    private fun checkPermission() {
        this.checkedPermissionStorage()
        this.checkedPermissionCamera()
    }
    //endregion

    //region widget
    fun clearWidget(vararg t: View): List<View>? {
        if (!t.any()) return null

        t.forEach {
            var text: String = ""
            if (it is CheckBox) {
                it.isChecked = false
                text = it.text.toString()
            }
            if (it is TextView) {
                it.apply {
                    this.text = text.toEditable()
                    this.setTextColor(Color.BLACK)
                    this.setHintTextColor(Color.BLUE)
                }
            }
            if (it is TextInputEditText) {
                it.apply {
                    this.text = text.toEditable()
                    this.setTextColor(Color.BLACK)
                    this.setHintTextColor(Color.LTGRAY)
                }
            }
            if (it is CustomInputText) {
                it.apply {
                    when (it.typeCustomType) {
                        TypeCustomInputTextType.TEXT -> this.text = text
                        TypeCustomInputTextType.NUMERIC -> {
                            when (customType) {
                                TypeCustomInputText.DOUBLE -> this.double = 0.toDouble()
                                TypeCustomInputText.INT -> this.int = 0
                            }
                        }
                        TypeCustomInputTextType.DATE -> this.date = HelperDate.nowDate()
                    }
                    this.setTextColor(Color.BLACK)
                    this.setHintTextColor(Color.LTGRAY)
                }
            }
            if (it is CheckBox) {
                it.apply {
                    this.text = text.toEditable()
                    this.setTextColor(Color.BLACK)
                }
            }
            if (it is ImageView) {
                it.setImageBitmap(null)
            }
        }
        return t.toList()
    }
    //endregion

    //region resource
    internal fun getDimensionUtil(@DimenRes value: Int): Int {
        return try {
            this.resources.getDimensionPixelOffset(value)
        } catch (e: Exception) {
            0
        }
    }
    //endregion

    //region selector
    protected fun setActivityAsSelector(
        @DrawableRes background: Int? = null,
        chooseView: View? = null
    ) {
        val scrollView: ScrollView? =
            onEventMakeActivityForSelector?.invoke(MainItemsLayoutScroll(this))
        root.apply {
            removeAllViews()
            if (scrollView != null && chooseView == null) root.addView(scrollView)
            else {
                val container = LinearLayout(this@BaseActivity).apply {
                    layoutParams =
                        makeCustomLayoutLinealLayout(TypeMakeLayoutParameter.WIDTH_MATCH_HEIGHT_MATCH)
                    orientation = LinearLayout.VERTICAL

                }
                if (chooseView != null) container.addView(chooseView)
                if (scrollView != null) container.addView(scrollView)
                root.addView(container)
            }
            if (background != null) this.background =
                BitmapFactory.decodeResource(resources, background)
                    .toDrawable(resources)
        }
    }
    //endregion

    //region helper
    protected fun getMessage(data: Exception?) {
        if (data == null) return
        Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
    }

    protected fun getMessage(data: String) {
        if (data.isEmpty()) return
        Toast.makeText(this, data, Toast.LENGTH_LONG).show()
    }

    protected fun getMessage(data: StringBuilder) {
        if (data.toString().isEmpty()) return
        Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show()
    }
    //endregion
}