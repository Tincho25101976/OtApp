package com.vsg.helper.ui.popup.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI.Static.setStatus
import com.vsg.helper.helper.font.FontManager
import com.vsg.helper.ui.popup.UICustomAlertDialogBase
import com.vsg.helper.ui.util.BaseActivity

class UICustomAlertDialogResult<TActivity>(activity: TActivity, onlySelect: Boolean = false) :
    UICustomAlertDialogBase<TActivity, UICustomAlertDialogParameter>(
        activity,
        R.layout.custom_alert_dialog_result
    ) where TActivity : BaseActivity {

    var onEventClickOK: (() -> Unit)? = null
    var onBeforeClose: (() -> Unit)? = null
    var onEventSetViewContainer: ((View) -> Unit)? = null
    var onAfterOK: (() -> Unit)? = null
    var onEventSetOKEnabled: (() -> Boolean)? = null

    init {
        onSetDialogView = { dialogView, data, alertDialog ->
            if (!onlySelect) {
                dialogView.findViewById<ImageView>(R.id.CustomAlertDialogResultCancel)
                    .setOnClickListener {
                        onBeforeClose?.invoke()
                        alertDialog.dismiss()
                    }
                val cmOK = dialogView.findViewById<ImageView>(R.id.CustomAlertDialogResultOK)
                cmOK.setStatus(onEventSetOKEnabled?.invoke() ?: true)
                cmOK.setOnClickListener {
                    onEventClickOK?.invoke()
                    onAfterOK?.invoke()
                    alertDialog.dismiss()
                }

            } else {
                dialogView.findViewById<RelativeLayout>(R.id.CustomAlertDialogResultCommand).apply {
                    visibility = View.GONE
                }
                dialogView.findViewById<LinearLayout>(R.id.CustomAlertDialogResultContainer).apply {
                    val layout = getCustomLayout(this, margin = true)
                    //layout.setMargins(0, 0, 0, 0)
                    layoutParams = layout
                }
            }
            addContainer(dialogView, data)
        }
    }

    private fun addContainer(view: View, data: UICustomAlertDialogParameter) {
        val container: LinearLayout =
            view.findViewById(R.id.CustomAlertDialogResultContainer)
        val inflate = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflate.inflate(data.layout, null)
        container.removeAllViews()
        container.addView(rowView, 0)
        if (rowView is ViewGroup) FontManager(activity).replaceFonts(rowView)
        onEventSetViewContainer?.invoke(view)
    }


}