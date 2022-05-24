package com.vsg.helper.ui.export

import android.app.Activity
import com.fasterxml.aalto.stax.InputFactoryImpl
import com.fasterxml.aalto.stax.OutputFactoryImpl
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.helper.permission.HelperPerminission.Static.checkedPermissionStorage
import java.io.File
import java.io.FileWriter
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter

class UIExportFormatXML<TEntity> :
    IUIExportFormat<TEntity>
        where TEntity : IEntityExport,
              TEntity : IEntity {

    override fun toFile(data: TEntity, activity: Activity, path: String): File? {
        if (!data.export().hasItems) return null
        if (path.isEmpty()) return null
        return try {
            if (!activity.checkedPermissionStorage()) return null
            val fileName = data.nameFile(ExportType.XML)
            val writer = getMapper()
            val ruta: File? = activity.getExternalFilesDir(path)
            ruta?.mkdirs()
            val file = File(ruta, fileName)
            try {
                FileWriter(file).use {
                    val xmlStreamWriter: XMLStreamWriter =
                        XMLOutputFactory.newInstance().createXMLStreamWriter(it)
                    xmlStreamWriter.writeStartDocument()
                    xmlStreamWriter.writeStartElement("root")
                    data.export().values.forEach { s ->
                        writer.writeValue(xmlStreamWriter, s)
                    }
                    xmlStreamWriter.writeEndElement()
                    xmlStreamWriter.writeEndDocument()
                    xmlStreamWriter.close()
                    it.flush()
                }
                file
            } catch (e: Exception) {
                throw e
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun toDataString(data: TEntity): String? {
        return try {
            val result = getMapper().writeValueAsString(data.export().values)
            result
        } catch (e: Exception) {
            ""
        }
    }

    private fun getMapper(): XmlMapper {
        val xmlFactory = XmlFactory(
            InputFactoryImpl() as XMLInputFactory,
            OutputFactoryImpl() as XMLOutputFactory
        )
        val module = JacksonXmlModule().apply { setDefaultUseWrapper(false) }
        val xmlMapper = XmlMapper(xmlFactory, module).apply {
            configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
            configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true)
            enable(SerializationFeature.INDENT_OUTPUT)
            enable(SerializationFeature.WRAP_ROOT_VALUE)
        }
        return xmlMapper
    }
}