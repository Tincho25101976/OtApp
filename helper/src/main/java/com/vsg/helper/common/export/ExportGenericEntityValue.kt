package com.vsg.helper.common.export

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = ExportGenericEntityValue.FIELD_NAME_ROOT)
class ExportGenericEntityValue(
    @field:JacksonXmlProperty(localName = FIELD_NAME_ENTITY, isAttribute = true)
    val export: String,
    @field:JacksonXmlProperty(localName = FIELD_NAME_NAME, isAttribute = true)
    val name: String,
    @field:JacksonXmlProperty(localName = FIELD_NAME_VALUE, isAttribute = true)
    val value: Any?
) {
    companion object {
        const val FIELD_NAME_ROOT = "export"
        const val FIELD_NAME_ENTITY = "entity"
        const val FIELD_NAME_NAME = "name"
        const val FIELD_NAME_VALUE = "value"
    }
}