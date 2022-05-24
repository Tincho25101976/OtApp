package com.vsg.helper.ui.report.pdf

import android.app.Activity
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.report.IEntityReport
import com.vsg.helper.helper.permission.HelperPerminission.Static.checkedPermissionStorage
import com.vsg.helper.ui.export.IUIEntityToFile
import java.io.File
import java.io.FileOutputStream

class UIReportFormatPDF<TEntity> :
    IUIEntityToFile<TEntity>
        where TEntity : IEntityReport,
              TEntity : IEntity {

    //region event
    var onEventSetTitle: ((Unit) -> String)? = null
    //endregion

    override fun toFile(data: TEntity, activity: Activity, path: String): File? {
        if (!data.report().hasItems) return null
        if (path.isEmpty()) return null
        return try {
            if (!activity.checkedPermissionStorage()) return null
            val fileName = data.nameFile(ExportType.XML)
            val ruta: File? = activity.getExternalFilesDir(path)
            ruta?.mkdirs()
            val file = File(ruta, fileName)
            try {
                val out = FileOutputStream(file).use {
                    val document = Document()
                    val pdf: PdfWriter = PdfWriter.getInstance(document, it)
                    document.open()
                    // pdf title:
                    val title = onEventSetTitle?.invoke(Unit)
                    if (title.isNullOrEmpty()) {
                        val titlePDF =
                            Paragraph(
                                title,
                                FontFactory.getFont("arial", 22F, Font.BOLD, BaseColor.RED)
                            )
                        document.add(titlePDF)
                    }
                    // pdf table:
                    val table = PdfPTable(2).apply {
                        data.report().items.filter { s -> s.isItemReport } .forEach { s ->
                            addCell(s.nameReport)
                            addCell(s.valueCast)
                        }
                    }
                    document.add(table)
                    document.close()
                }
                file
            } catch (e: Exception) {
                throw e
            }
        } catch (e: Exception) {
            null
        }
    }
}