package com.vsg.helper.common.export

interface IDataExportToFormat<T> where T: IExportSimpleFormat {
    fun getDataSimpleFormat(): List<T>
}