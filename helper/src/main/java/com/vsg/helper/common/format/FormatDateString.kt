package com.vsg.helper.common.format

import com.vsg.helper.common.utilEnum.IValue

enum class FormatDateString(override val value: Int, val data: String): IValue {
    SIMPLE(value = 1, data = "dd/MM/yyyy"),
    COMPLETO(value = 2, data = "dd/MM/yyyy HH:mm:ss"),
    CREATE_DATE(value = 3, data = "dd/MM/yyyy HH:mm"),
    FULL(value = 4, data = "EEEE, dd MMMM yyyy HH:mm:ss"),
    FILE(value = 5, data = "yyyyMMdd_HHmmss"),
    AUDIT(value = 6, data = "dd/MM/yyyy HH:mm:ss.SSS")
}