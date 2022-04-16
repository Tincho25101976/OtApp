package com.vsg.helper.ui.popup

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.vsg.helper.R
import com.vsg.helper.common.popup.IPopUpParameter
import com.vsg.helper.helper.font.FontManager
import com.vsg.helper.ui.util.BaseActivity


abstract class UICustomAlertDialogBase<TActivity, TParameter>(
    protected val activity: TActivity,
    @LayoutRes private val layout: Int
) where TActivity : BaseActivity, TParameter : IPopUpParameter {

    private var alertDialog: AlertDialog? = null
    protected var dialogView: View? = null

    var onSetDialogView: ((View, TParameter, AlertDialog) -> Unit)? = null

    fun getContext(): Context? = alertDialog?.context
    private var currentHeight: Int = 0
    private var currentWidth: Int = 0
    val height: Int
        get() = currentHeight
    val width: Int
        get() = currentWidth

    fun make(data: TParameter?) {
        if (data == null) return
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.CustomAlertDialog)
        val view: ViewGroup = activity.findViewById(android.R.id.content)
        dialogView = LayoutInflater.from(activity).inflate(layout, view, false)

        FontManager(activity).replaceFonts(dialogView as ViewGroup)

        builder.setView(dialogView)
        alertDialog = builder.create()

        onSetDialogView?.invoke(dialogView!!, data, alertDialog!!)

        alertDialog?.setCanceledOnTouchOutside(data.canceledOnTouchOutside)
        alertDialog?.show()
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)

        currentWidth = (dm.widthPixels * data.factorWidth).toInt()
        currentHeight = (dm.heightPixels * data.factorHeight).toInt()
        alertDialog?.window?.setLayout(currentWidth, currentHeight)
    }

    fun dismiss() {
        if (alertDialog != null) alertDialog?.dismiss()
    }

    protected fun getCustomLayout(
        view: View,
        rule: Int = 0,
        margin: Boolean = false
    ): RelativeLayout.LayoutParams {
        val layout: RelativeLayout.LayoutParams = view.layoutParams as RelativeLayout.LayoutParams
        if (rule > 0) layout.addRule(rule)
        if (margin) layout.setMargins(
            DEFAULT_MARGIN,
            DEFAULT_MARGIN,
            DEFAULT_MARGIN,
            DEFAULT_MARGIN
        )
        layout.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layout.width = ViewGroup.LayoutParams.MATCH_PARENT
        return layout
    }


    companion object Static {
        const val DEFAULT_MARGIN: Int = 10
    }
}