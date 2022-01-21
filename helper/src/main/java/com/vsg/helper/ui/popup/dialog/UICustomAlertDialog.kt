package com.vsg.helper.ui.popup.dialog

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.vsg.helper.R
import com.vsg.helper.common.popup.IPopUpData
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.helper.ui.popup.UICustomAlertDialogBase
import com.vsg.helper.ui.util.BaseActivity

class UICustomAlertDialog<TActivity>(activity: TActivity) : UICustomAlertDialogBase<TActivity, IPopUpData>(
    activity,
    R.layout.custom_alert_dialogo
) where TActivity : BaseActivity {

    var onEventClickOK: ((Boolean) -> Unit)? = null

    init {
        this.onSetDialogView = { dialogView, data, alertDialog ->
            dialogView.findViewById<ImageView>(R.id.CustomAlertDialogExit)
                .setOnClickListener {
                    alertDialog.dismiss()
                }
            dialogView.findViewById<ImageView>(R.id.CustomAlertDialogOK).apply {
                visibility = when (data.commandOK) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
                setOnClickListener {
                    onEventClickOK?.invoke(data.commandOK)
                    alertDialog.dismiss()
                }
            }
            dialogView.findViewById<TextView>(R.id.CustomAlertDialogTitle).apply {
                text = when (data.title.isEmpty()) {
                    true -> ""
                    false -> data.title.toTitleSpanned(false)
                }
            }
            dialogView.findViewById<TextView>(R.id.CustomAlertDialogBody).apply {
                text = when (data.isSpanned()) {
                    false -> data.body
                    true -> data.toHtml
                }
            }
            dialogView.findViewById<ImageView>(R.id.CustomAlertDialogIcon).apply {
                setImageBitmap(null)
                if (data.isBitmap) this.setImageBitmap(data.bitmap)
                else if (data.icon > 0) this.setImageResource(data.icon)

            }
        }
    }
}