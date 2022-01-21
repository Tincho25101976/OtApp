package com.vsg.helper.helper.file

import com.vsg.helper.helper.file.HelperFile.Static.getMimeType
import java.text.SimpleDateFormat
import java.util.*


class TypeTempValue(var prefix: String, var format: String) {
    var suffix: String = ".$format"
    fun type(): String = format.getMimeType()
    private fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss");
        val time = Calendar.getInstance().time
        return simpleDateFormat.format(time);
    }
    fun prefixWithTime(): String = this.prefix + getCurrentTime() + '_';
}