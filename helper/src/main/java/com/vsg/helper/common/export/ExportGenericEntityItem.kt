package com.vsg.helper.common.export

data class ExportGenericEntityItem<T>(
    val name: String,
    val allowNull: Boolean = false,
    val value: T? = null
)