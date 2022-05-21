package com.vsg.helper.ui.export

import com.fasterxml.aalto.stax.InputFactoryImpl
import com.fasterxml.aalto.stax.OutputFactoryImpl
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.vsg.helper.common.export.IEntityExport
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory

class UIExportXML<T> where T : IEntityExport {
    private fun getXMLMapper(): XmlMapper {
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

    fun toJson(data: T?): String {
        if (data == null) return ""
        return try {
            val mapper = ObjectMapper()
            mapper.configure(SerializationFeature.INDENT_OUTPUT, false)
            mapper.writeValueAsString(data)
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    fun toXML(data: T?): String {
        if (data == null) return ""
        val xml: String
        try {
            xml = getXMLMapper().writeValueAsString(data)
        } catch (e: Exception) {
            throw e
        }
        return xml
    }

    private fun toMap(xml: String, type: Class<T>): T? {
        val xmlMapper: ObjectMapper = XmlMapper()
        val value: T = xmlMapper.readValue(xml, type)
        return value
    }
}