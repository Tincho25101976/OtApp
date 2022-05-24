package com.vsg.helper.ui.popup.export

import android.app.Activity
import android.os.Environment
import android.widget.ImageView
import com.vsg.helper.R
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.helper.file.HelperFile.Static.sendFile
import com.vsg.helper.ui.export.IUIExportToFile
import com.vsg.helper.ui.export.UIExportFormatCSV
import com.vsg.helper.ui.export.UIExportFormatJson
import com.vsg.helper.ui.export.UIExportFormatXML
import com.vsg.helper.ui.popup.UICustomAlertDialogItemBase

class UICustomAlertDialogExport<TActivity, TEntity>(
    activity: TActivity,
    data: UICustomAlertDialogExportParameter
) :
    UICustomAlertDialogItemBase<TActivity, TEntity, ExportType, UICustomAlertDialogExportParameter>(
        activity,
        data,
        R.layout.custom_dialog_result_export
    )
        where TActivity : Activity,
              TEntity : IEntityExport,
              TEntity : IEntity {

    //region event
    var onEventSetIUIExportToFile: ((ExportType) -> IUIExportToFile<TEntity>)? = null
    //endregion

    init {
        onSetComponents = { dialogView, _, alertDialog ->
            val list = listOf(
                Component(
                    ExportType.XML,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_export_xml),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_export_xml),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_export_xml)
                ),
                Component(
                    ExportType.JSON,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_export_json),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_export_json),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_export_json)
                ),
                Component(
                    ExportType.CSV,
                    dialogView.findViewById(R.id.ll_custom_dialog_result_export_csv),
                    dialogView.findViewById(R.id.tv_custom_dialog_result_export_csv),
                    dialogView.findViewById(R.id.iv_custom_dialog_result_export_csv)
                )
            )
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

    fun export(e: TEntity, type: ExportType) {
        try {
            val directory: String = Environment.DIRECTORY_DOCUMENTS
            val iExport: IUIExportToFile<TEntity> = when (type) {
                ExportType.XML -> UIExportFormatXML()
                ExportType.JSON -> UIExportFormatJson()
                ExportType.CSV -> UIExportFormatCSV()
                ExportType.PDF -> null
            } ?: return
            this.activity.sendFile(iExport.toFile(e, this.activity, directory))
        } catch (e: Exception) {
        }
    }
}