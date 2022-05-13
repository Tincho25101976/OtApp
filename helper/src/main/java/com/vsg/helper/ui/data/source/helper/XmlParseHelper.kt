package com.vsg.helper.ui.data.source.helper

import android.text.Spanned
import com.vsg.helper.common.model.IEntityParse
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.helper.ui.data.ILog
import com.vsg.helper.ui.data.IReadToList
import com.vsg.helper.ui.data.log.CustomLog
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream

class XmlParseHelper<T>(var item: T) : ILog, IReadToList<T>
        where T : IEntityParse<T> {

    var list = ArrayList<HashMap<String?, String?>>()
    var cast: MutableList<T> = mutableListOf()
    private val log: CustomLog = CustomLog()

    fun getParse(file: InputStream) = try {
        log.addLog("Inicio del proceso de mapeo el archivo XML".toTitleSpanned(false))
        var mapping: HashMap<String?, String?>? = HashMap()
        cast = mutableListOf()
        val parserFactory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
        val parser: XmlPullParser = parserFactory.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
        parser.setInput(file, null)
        var tag: String?
        var text = ""
        var event = parser.eventType
        log.addLog("Parse START XML".castToHtml())
        while (event != XmlPullParser.END_DOCUMENT) {
            tag = parser.name
            when (event) {
                XmlPullParser.START_TAG -> if (tag == item.tag) {
                    mapping = HashMap()
                }
                XmlPullParser.TEXT -> {
                    text = parser.text
                }
                XmlPullParser.END_TAG -> {
                    item.getFields().forEach {
                        when (tag) {
                            it -> mapping!![it] = text
                        }
                    }
                    if (item.tag == tag && mapping != null) {
                        list.add(mapping)
                        cast.add(item.cast(mapping))
                    }
                }
            }
            event = parser.next()
        }
        log.addLog("Parse END XML".castToHtml())
        log.addLog("COUNT: ${cast.count()}".castToHtml())
    } catch (e: IOException) {
        log.addLog(e.message?.castToHtml())
        e.printStackTrace()
    } catch (e: XmlPullParserException) {
        log.addLog(e.message?.castToHtml())
        e.printStackTrace()
    }

    override fun getLog(): Spanned = log.getAndNew()
    override fun readToList(): MutableList<T> = cast
}