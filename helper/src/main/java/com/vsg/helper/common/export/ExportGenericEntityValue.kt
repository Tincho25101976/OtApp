package com.vsg.helper.common.export

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "items")
data class ExportGenericEntityValue(
    @field:JacksonXmlProperty(localName = "export", isAttribute = true)
    val export: String,
    @field:JacksonXmlProperty(localName = "name", isAttribute = true)
    val name: String,
    @field:JacksonXmlProperty(localName = "value", isAttribute = true)
    val value: Any?
)