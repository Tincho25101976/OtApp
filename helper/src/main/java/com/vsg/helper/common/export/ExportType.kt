package com.vsg.helper.common.export

enum class ExportType(var data: String, var extension: String, var value: Int) {
    XML("Xml", "xml", 1),
    JSON("Json", "json", 2),
    CSV("Csv", "csv", 3);
}