package com.vsg.helper.ui.popup.export

import android.app.Activity
import android.widget.ImageView
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.R
import com.vsg.helper.ui.popup.UICustomAlertDialogItemBase

class UICustomAlertDialogExport<TActivity, TModel>(activity: TActivity,
                                                   data: UICustomAlertDialogExportParameter
) :
    UICustomAlertDialogItemBase<TActivity, TModel, ExportType, UICustomAlertDialogExportParameter>(
        activity,
        data,
        R.layout.custom_dialog_result_export) where TActivity : Activity {

    init {
        onSetComponents = { dialogView, _, alertDialog ->
            val list = listOf(Component(ExportType.XML,
                                        dialogView.findViewById(R.id.ll_custom_dialog_result_export_xml),
                                        dialogView.findViewById(R.id.tv_custom_dialog_result_export_xml),
                                        dialogView.findViewById(R.id.iv_custom_dialog_result_export_xml)),
                              Component(ExportType.JSON,
                                        dialogView.findViewById(R.id.ll_custom_dialog_result_export_json),
                                        dialogView.findViewById(R.id.tv_custom_dialog_result_export_json),
                                        dialogView.findViewById(R.id.iv_custom_dialog_result_export_json)),
                              Component(ExportType.CSV,
                                        dialogView.findViewById(R.id.ll_custom_dialog_result_export_csv),
                                        dialogView.findViewById(R.id.tv_custom_dialog_result_export_csv),
                                        dialogView.findViewById(R.id.iv_custom_dialog_result_export_csv)))
            dialogView.findViewById<ImageView>(R.id.iv_custom_dialog_result_export_exit)
                .setOnClickListener {
                    alertDialog.dismiss()
                }
            list
        }
        onSetLayoutMain = { dialogView ->
            dialogView.findViewById(R.id.ll_custom_dialog_result_export)
        }
    }
}