package com.vsg.helper.ui.popup

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.vsg.helper.ui.popup.parameter.UICustomAlertDialogParameterBase
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI.Static.resize
import com.vsg.helper.helper.font.FontManager
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustomSpan
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel

abstract class UICustomAlertDialogItemBase<TActivity, TModel, TEnum, TParameter>(
    protected var activity: TActivity,
    protected val data: TParameter,
    @LayoutRes val layout: Int
)
        where TActivity : Activity, TParameter : UICustomAlertDialogParameterBase<TEnum> {

    var onEventClickItem: ((TEnum, TModel?) -> Unit)? = null

    private lateinit var linearLayoutMain: LinearLayout
    private lateinit var components: List<Component<TEnum>>

    var onSetComponents: ((View, TModel, AlertDialog) -> List<Component<TEnum>>)? = null
    var onSetLayoutMain: ((View) -> LinearLayout)? = null

    fun make(item: TModel?) {
        if (item == null || !data.isProcess()) return
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)
        val view: ViewGroup = activity.findViewById(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(activity).inflate(layout, view, false)



        if (data.title.isNotEmpty()) {
            val title = SpannableString(data.title)
            if (title.isNotEmpty()) {
                listOf(
                    ForegroundColorSpan(Color.DKGRAY),
                    activity.typeFaceCustomSpan(Typeface.BOLD_ITALIC),
                    UnderlineSpan(),
                    StyleSpan(Typeface.BOLD_ITALIC),
                    RelativeSizeSpan(data.getSizeTitleFontWhitFactor())
                ).forEach {
                    title.setSpan(it, 0, title.length, 0)
                }
                builder.setTitle(title)
            }
        }
        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()

        components = onSetComponents?.invoke(dialogView, item, alertDialog) ?: return
        if (!components.any()) return
        val reDimSize = components.groupBy { it.image.layoutParams.height }
            .maxOf { it.key } != data.sizeImage && data.sizeImage > 0

        linearLayoutMain = onSetLayoutMain?.invoke(dialogView) ?: return
        linearLayoutMain.removeAllViews()
        components.forEach {
            if (data.menu().any { s -> s == it.type }) {
                it.text.setTextColor(Color.BLACK)
                if (data.titles.any { s -> s.key == it.type }) {
                    it.text.text = data.titles[it.type]
                }
                if (data.images.any { s -> s.key == it.type }) {
                    it.image.setImageBitmap(null)
                    val temp = data.images[it.type]
                    if(temp != null) it.image.setImageResource(temp)
                }
                if (reDimSize) {
                    val size = data.sizeImage.toPixel(activity)
                    it.image.resize(size)
                }
                it.main.setOnClickListener { _ ->
                    onEventClickItem?.invoke(it.type, item)
                    alertDialog.dismiss()
                }
                linearLayoutMain.addView(it.main)
            }
        }
        if (reDimSize) {
            data.factorHeight = data.calculatedFactorHeight(data.sizeImage)
            val sizeFont = data.getSizeFontWhitFactor(data.sizeImage)
            components.forEach { it.text.textSize = sizeFont }

        }
        FontManager(activity).replaceFonts(dialogView as ViewGroup)

        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val width = (dm.widthPixels * data.factorWidth).toInt()
        val height = (dm.heightPixels * data.factorHeight).toInt()
        alertDialog.window?.setLayout(width, height)
    }

    data class Component<TEnum>(
        val type: TEnum,
        val main: LinearLayout,
        val text: TextView,
        val image: ImageView
    )
}