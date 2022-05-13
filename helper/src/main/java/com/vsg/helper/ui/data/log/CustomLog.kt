package com.vsg.helper.ui.data.log

import android.text.Spanned
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.helper.string.HelperString.Static.toTagBold
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CustomLog {
    private var sb: StringBuilder = StringBuilder()
    private var count: Int = 0

    private val format: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    private fun getCount(): Spanned {
        val i = ++count
        var len = 3
        if (i in 1..9) len = 3
        if (i in 10..99) len = 2
        if (i in 100..999) len = 1
        if (i > 1000) len = 0
        return StringBuilder().toTagBold("${i.toString().padStart(len, '0')}) => ").castToHtml()
    }

    fun addLog(data: Spanned?) {
        if (data == null || data.isEmpty()) return
        val now = LocalDateTime.now()
        sb.append(getCount())
        sb.append(now.format(format)).appendLine()
        sb.append(data).appendLine()
        sb.append(LINE).appendLine()
    }

    fun addLog(caption: String, item: String) {
        val data = StringBuilder().toLineSpanned(caption, item).castToHtml()
        addLog(data)
    }

    fun clear() {
        sb = StringBuilder()
        count = 0
    }

    fun getLog(): Spanned = sb.castToHtml()
    fun getAndNew(): Spanned {
        val data = getLog()
        clear()
        return data
    }

    override fun toString(): String = sb.toString()

    companion object Static {
        const val LINE: String = "---------------------------"
    }
}