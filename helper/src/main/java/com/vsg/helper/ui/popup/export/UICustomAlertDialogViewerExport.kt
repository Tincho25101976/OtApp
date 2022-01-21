package com.vsg.helper.ui.popup.export

import android.app.Activity
import android.app.AlertDialog
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI.Static.setStatus
import com.vsg.helper.helper.font.FontManager
import com.vsg.helper.helper.permission.HelperPerminission.Static.checkedPermissionStorage

class UICustomAlertDialogViewerExport<TActivity>(
    val activity: TActivity,
    private val data: UICustomAlertDialogExportParameter
) where TActivity : Activity {

    var onClickItem: ((TActivity, Window, View) -> Unit)? = null
    var onGetLayout: ((TActivity, LinearLayout) -> View)? = null
    private lateinit var tView: LinearLayout

    fun make() {
        if (!activity.checkedPermissionStorage() && this.data.isProcess()) return
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(this.activity, R.style.CustomAlertDialog)
        val view: ViewGroup = this.activity.findViewById(android.R.id.content)
        val dialogView: View = LayoutInflater.from(this.activity)
            .inflate(R.layout.custom_dialog_result_export_viewer, view, false)

        builder.setView(dialogView)
        val alertDialog: AlertDialog = builder.create()
        this.tView = dialogView.findViewById(R.id.ll_custom_dialog_result_export_viewer_main)

        val item: View? = this.onGetLayout?.invoke(activity, LinearLayout(activity))
        this.tView.removeAllViews()
        this.tView.addView(
            item,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialogView.findViewById<ImageView>(R.id.iv_custom_dialog_result_export_viewer_exit).apply {
            setStatus(item != null)
            setOnClickListener {
                onClickItem?.invoke(activity, alertDialog.window!!, item!!)
                alertDialog.dismiss()
            }
        }

        FontManager(this.activity).replaceFonts(dialogView as ViewGroup)

        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.show()
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val width = (dm.widthPixels * data.factorWidth).toInt()
        val height = (dm.heightPixels * data.factorHeight).toInt()
        alertDialog.window?.setLayout(width, height)
    }
}