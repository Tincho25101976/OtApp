package com.vsg.helper.ui.report.pdf

import android.app.Activity
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.draw.LineSeparator
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.report.IEntityReport
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.helper.permission.HelperPermission.Static.checkedPermissionStorage
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
            val fileName = data.nameFile(ExportType.PDF, true)
            val ruta: File? = activity.getExternalFilesDir(path)
            ruta?.mkdirs()
            val file = File(ruta, fileName)
            FileOutputStream(file).use {
                val document = Document()
                val writer = PdfWriter.getInstance(document, it)
                document.open()

                // Document Settings
                document.pageSize = PageSize.A4
                val margin = 5F;
                document.setMargins(margin, margin, margin, margin)
                document.addCreationDate()
                document.addAuthor(activity.packageName)
                document.addCreator(activity.packageName)

                // Document Font
                val mColorAccent = BaseColor(0, 153, 204, 255)
                val mHeadingFontSize = 20.0f
                val mValueFontSize = 26.0f
                val fontBase = BaseFont.createFont(
                    BaseFont.HELVETICA,
                    BaseFont.CP1252,
                    BaseFont.EMBEDDED
                )

                // Line Separator
                val lineSeparator = LineSeparator().apply {
                    lineColor = BaseColor(87, 68, 64, 68)
                    lineWidth = 3F
                }

                // Document Title:
                val title = onEventSetTitle?.invoke(Unit)
                if (!title.isNullOrEmpty()) {
                    val backColor = BaseColor.LIGHT_GRAY
                    // AddTitle:
                    val fontTitle =
                        Font(fontBase, 36F, Font.BOLD or Font.UNDERLINE, BaseColor.WHITE)
                    val titlePDF =
                        Paragraph(title.uppercase(), fontTitle).apply {
                            alignment = Element.ALIGN_LEFT
                            setLeading(50F, 0F)
                        }

                    val table = PdfPTable(2).apply {
                        widthPercentage = 100F
                        this.addCell(PdfPCell().apply {
                            paddingLeft = 25F
                            backgroundColor = backColor
                            verticalAlignment = Element.ALIGN_MIDDLE
                            horizontalAlignment = Element.ALIGN_LEFT
                            border = Rectangle.NO_BORDER
                            addElement(titlePDF)
                        })

                        // AddImage:
                        data.report().items.filter { s -> s.isByteArray }.forEach { s ->
                            val img: Image = Image.getInstance(s.valueByteArray)
                            val w = 200F
                            val h = 200F
                            val template: PdfTemplate = writer.directContent.createTemplate(w, h).apply {
                                ellipse(0F, 0F, w, h)
                                clip()
                                newPath()
                                addImage(
                                    img,
                                    w.toDouble(),
                                    0.0,
                                    0.0,
                                    h.toDouble(),
                                    0.0,
                                    0.0
                                )
                            }
                            val jpg: Image = Image.getInstance(template).apply {
                                scaleToFit(w, h)
                                alignment = Image.ALIGN_CENTER or Image.ALIGN_MIDDLE
                            }
                            val cellImage = PdfPCell(jpg).apply {
                                backgroundColor = backColor
                                verticalAlignment = Element.ALIGN_MIDDLE
                                horizontalAlignment = Element.ALIGN_CENTER
                                border = Rectangle.NO_BORDER
                                setPadding(25F)
                            }
                            addCell(cellImage)
                        }
                    }
                    document.add(table)
                }

                //val marginData = 30F
                document.add(Chunk("\n\n"))
                data.report().items.filter { s -> s.isItemReport }.forEach { s ->
                    document.addTitleAndDataLine(s.nameReport, s.valueCast, fontBase)
                }


                // pdf table:
//                val fontCellTitle =
//                    Font(fontBase, 16F, Font.UNDERLINE or Font.BOLDITALIC, BaseColor.BLACK)
//                val fontCellValue =
//                    Font(fontBase, 14F, Font.NORMAL, BaseColor.BLACK)
//                val table = PdfPTable(2).apply {
//                    data.report().items.filter { s -> s.isItemReport }.forEach { s ->
//                        addCell(getCell("${s.nameReport}:", BaseColor.LIGHT_GRAY, fontCellTitle))
//                        addCell(getCell(s.valueCast, font = fontCellValue))
//                    }
//                }
//
//                document.addSeparator()
//                document.add(table)
//                document.addSeparator()

                // close:
                document.close()
            }
            file
        } catch (e: Exception) {
            null
        }
    }

    private fun getCell(
        value: String,
        color: BaseColor = BaseColor.WHITE,
        font: Font? = null
    ): PdfPCell {
        val cell = PdfPCell((font == null) then Phrase(value) or Phrase(value, font))
        cell.apply {
            border = Rectangle.BOX
            backgroundColor = color
            minimumHeight = 40F
            verticalAlignment = Element.ALIGN_MIDDLE
            paddingRight = 5F
            paddingLeft = 5F
        }
        return cell
    }

    private fun Document.addSeparator(beforeLines: Int = 1, afterLines: Int = 1) {
        val lineSeparator = LineSeparator().apply {
            lineColor = BaseColor(87, 68, 64, 68)
            lineWidth = 3F
        }
//        (1..beforeLines).forEach { _ -> this.add(Chunk()) }
        this.add(Chunk("\n"))
        this.add(lineSeparator)
//        (1..afterLines).forEach { _ -> this.add(Chunk()) }
    }

    private fun Document.addTitleAndDataLine(
        title: String,
        data: String,
        font: BaseFont,
        space: Float = 2F
    ) {
        val fontCellTitle =
            Font(font, 18F, Font.UNDERLINE or Font.BOLDITALIC, BaseColor.BLUE)

        this.add(Paragraph("${title}:", fontCellTitle).apply {
            alignment = Element.ALIGN_LEFT
            spacingBefore = space
        })
        val fontCellValue =
            Font(font, 14F, Font.NORMAL, BaseColor.BLACK)
        this.add(Paragraph(data, fontCellValue).apply {
            alignment = Element.ALIGN_RIGHT
            spacingAfter = space
        })
        this.addSeparator()
    }
}