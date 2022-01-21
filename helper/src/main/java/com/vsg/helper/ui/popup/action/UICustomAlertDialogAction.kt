package com.vsg.helper.ui.popup.action

import android.app.Activity
import android.widget.ImageView
import com.vsg.helper.R
import com.vsg.helper.ui.popup.UICustomAlertDialogItemBase

class UICustomAlertDialogAction<TActivity, TModel>(
    activity: TActivity,
    data: UICustomAlertDialogActionParameter
) :
    UICustomAlertDialogItemBase<TActivity, TModel, UICustomAlertDialogActionType, UICustomAlertDialogActionParameter>(
        activity,
        data,
        R.layout.custom_dialog_result_crud
    ) where TActivity : Activity {

    init {
        this.onSetComponents = { dialogView, _, alertDialog ->
            val list = listOf(
                Component(
                    UICustomAlertDialogActionType.INSERT,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_crud_insert),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_crud_insert),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_crud_insert)
                ),
                Component(
                    UICustomAlertDialogActionType.UPDATE,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_crud_update),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_crud_update),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_crud_update)
                ),
                Component(
                    UICustomAlertDialogActionType.DELETE,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_crud_delete),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_crud_delete),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_crud_delete)
                ),
                Component(
                    UICustomAlertDialogActionType.VIEW,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_crud_view),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_crud_view),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_crud_view)
                ),
                Component(
                    UICustomAlertDialogActionType.EXPORT,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_crud_export),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_crud_export),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_crud_export)
                ),
            )
            dialogView.findViewById<ImageView>(R.id.iv_custom_dialog_result_crud_exit)
                .setOnClickListener {
                    alertDialog.dismiss()
                }
            list
        }
        this.onSetLayoutMain = { dialogView ->
            dialogView.findViewById(R.id.ll_custom_dialog_result_crud)
        }
    }
}