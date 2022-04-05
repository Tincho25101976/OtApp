package com.vsg.ot.ui.autoFill

import android.graphics.*
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.viewModel.IViewModelCRUD
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toBitmap
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toRotate
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toScale
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.helper.string.HelperString.Static.capitalizeWords
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.util.helper.HelperNumeric.Companion.toPadStart
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import kotlin.random.Random

abstract class AutoFillDataBase(protected val app: BaseActivity) {
    var onEventProgress: ((Int, Int) -> Unit)? = null

    private var utilPalabras: Map<Int, String> = mutableMapOf()
    private var utilChar: MutableList<UtilCharMap> = mutableListOf()
    protected var utilStreets: Map<Int, String> = mutableMapOf()

    private val maxStreet: Int
        get() = utilStreets.maxOf { it.key }

    init {
        makeUtilChar()
    }

    //region viewModel
    protected fun <TParent, TViewModelParent> makeViewModel(type: Class<TViewModelParent>): TViewModelParent
            where TViewModelParent : ViewModel,
                  TViewModelParent : IViewModelCRUD<TParent>,
                  TParent : IEntity {
        return run { ViewModelProvider(app).get(type) }
    }
    //endregion

    //region util
    protected fun getRandomDate(from: Int = 1960, to: Int = 2000): Date {
        var toYear: Int = to
        if (to < from) toYear = from
        val year = when (from == toYear) {
            true -> from
            false -> Random.nextInt(from, toYear)
        }
        val month = Random.nextInt(1, 12)
        val day = when (month) {
            2 -> Random.nextInt(
                1, when (year.isYearLeap()) {
                    true -> 29
                    false -> 28
                }
            )
            4, 6, 9, 11 -> Random.nextInt(1, 30)
            else -> Random.nextInt(1, 31)
        }
        val c: Calendar = Calendar.getInstance()
        c.set(year, month, day)
        return c.time
    }

    private fun Int.isYearLeap(): Boolean {
        return ((this % 4) == 0 && (this % 100 != 0) && (this % 400 == 0))
    }

    protected fun getRandomCode(
        upper: Boolean = true,
        symbols: Boolean = false,
        numeric: Boolean = true,
        lower: Boolean = true
    ): String {
        val min = Random.nextInt(10, 15)
        val max = Random.nextInt(min + 1, 50)
        val c = Random.nextInt(min, max)
        val data = StringBuilder()
        val temp = filterUtilChar(symbols, numeric, lower, upper)
        repeat(c) {
            data.append(randomUtilChar(temp))
        }
        return data.toString()
    }

    protected fun getRandomChars(
        @androidx.annotation.IntRange(
            from = 1L,
            to = 50L
        ) range: Int,
        upper: Boolean = true,
        symbols: Boolean = false,
        numeric: Boolean = false,
        lower: Boolean = false
    ): String {
        val data = StringBuilder()
        val temp = filterUtilChar(symbols, numeric, lower, upper)
        repeat(range) {
            data.append(randomUtilChar(temp))
        }
        return when (upper) {
            true -> data.toString().toUpperCase(Locale.ROOT)
            false -> data.toString()
        }
    }

    protected fun getMakeListFromFile(filename: String): Map<Int, String> {
        var data: Map<Int, String>? = null
        val list: MutableList<String> = mutableListOf()
        var i = 0
        try {
            app.assets.open("txt/${filename}.txt").use {
                BufferedReader(InputStreamReader(it)).use { br ->
                    var read: String?
                    do {
                        read = br.readLine()
                        if (read != null) list.add(read)
                    } while (read != null)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (list.any()) {
            data = list.associateBy { i++ }
        }
        return data ?: mutableMapOf()
    }

    protected fun getRandomDescription(): String {
        if (!utilPalabras.any()) return ""
        val sb = StringBuilder()
        val min: Int = utilPalabras.minOf { it.key }
        val max: Int = utilPalabras.maxOf { it.key }
        repeat(Random.nextInt(5)) {
            val temp = utilPalabras[Random.nextInt(min, max)]
            if (!temp.isNullOrEmpty()) sb.appendLine(temp)
        }
        return sb.toString()
    }

    protected fun getPictureArray(): ByteArray? {
        return getPicture().toArray()
    }

    private fun getPicture(): Bitmap {
        var data: Bitmap?
        do {
            val value = Random.nextInt(1, 1501).toString().padStart(5, '0')
            val file = "picture/${value}.png"
            data = try {
                app.assets.open(file).use {
                    BitmapFactory.decodeStream(it)
                }
            } catch (ex: IOException) {
                null
            }
        } while (data == null)
        return data
    }

    //region utilChar
    private fun randomUtilChar(data: List<UtilCharMap>): String =
        when (data.any()) {
            true -> data.firstOrNull {
                it.index == Random.nextInt(
                    minUtilChar(data),
                    maxUtilChar(data)
                )
            }
                ?.data ?: "0"
            false -> "0"
        }

    private fun minUtilChar(data: List<UtilCharMap>): Int =
        when (data.any()) {
            true -> data.minOf { it.index }
            false -> 0
        }

    private fun maxUtilChar(data: List<UtilCharMap>): Int =
        when (data.any()) {
            true -> data.maxOf { it.index }
            false -> 0
        }

    private fun filterUtilChar(
        symbols: Boolean = true,
        numeric: Boolean = true,
        lower: Boolean = true,
        upper: Boolean = true
    ): List<UtilCharMap> {
        if (!utilChar.any()) {
            makeUtilChar(symbols, numeric, lower)
            return utilChar
        }
        val data: MutableList<UtilCharMap> = mutableListOf()
        if (symbols) data.addAll(utilChar.filter { it.type == TypeUtilChar.SYMBOL })
        if (numeric) data.addAll(utilChar.filter { it.type == TypeUtilChar.NUMERIC })
        if (lower) data.addAll(utilChar.filter { it.type == TypeUtilChar.LOWER })
        if (upper) data.addAll(utilChar.filter { it.type == TypeUtilChar.UPPER })
        return data
    }

    private fun getParcialUtilChar(
        count: Int,
        data: Array<IntRange>,
        type: TypeUtilChar
    ): Pair<Int, List<UtilCharMap>> {
        val temp: MutableList<UtilCharMap> = mutableListOf()
        var i = count
        data.forEach {
            it.forEach { n -> temp.add(UtilCharMap(i++, n.toChar().toString(), type)) }
        }
        return Pair(i, temp)
    }

    private fun makeUtilChar(
        symbols: Boolean = true,
        numeric: Boolean = true,
        lower: Boolean = true
    ) {
        utilChar = mutableListOf()
        var i = 0
        if (symbols) {
            val temp = getParcialUtilChar(i, arrayOf((33..47), (91..93)), TypeUtilChar.SYMBOL)
            utilChar.addAll(temp.second)
            i = temp.first
        }
        if (numeric) {
            val temp = getParcialUtilChar(i, arrayOf((48..57)), TypeUtilChar.NUMERIC)
            utilChar.addAll(temp.second)
            i = temp.first
        }
        if (lower) {
            val temp = getParcialUtilChar(i, arrayOf((97..122)), TypeUtilChar.LOWER)
            utilChar.addAll(temp.second)
            i = temp.first
        }
        val temp = getParcialUtilChar(i, arrayOf((65..90)), TypeUtilChar.UPPER)
        utilChar.addAll(temp.second)
    }
    //endregion

    protected fun makePicture(
        text: String,
        height: Int = IMAGEN_SIZE,
        width: Int = IMAGEN_SIZE
    ): ByteArray? {
        if (text.isEmpty()) return null
        var addBitmap: Bitmap?
        val temp: Bitmap = text.toBitmap(
            height,
            width,
            85F,
            Color.BLACK,
            Color.LTGRAY,
            app.typeFaceCustom(Typeface.BOLD_ITALIC)
        ) ?: return null
        try {
            addBitmap = temp.copy(Bitmap.Config.ARGB_8888, true).toRotate(270F)
            val canvas = Canvas(addBitmap!!)
            val scale = 20 * IMAGEN_SIZE / 100
            val littleBitmap: Bitmap? = getPicture().toScale(scale)
            canvas.drawBitmap(littleBitmap!!, 10F, 10F, null)
        } catch (e: Exception) {
            addBitmap = null
        }
        return addBitmap.toArray() ?: temp.toArray()
    }

    protected fun makeLocation(): String {
        var one: String?
        do {
            one = utilStreets[Random.nextInt(1, maxStreet)]
        } while (one.isNullOrEmpty())
        return "$one ${Random.nextInt(1, 9999)}".capitalizeWords()
    }

    protected fun getMessage(
        count: Int = 0,
        item: String = "",
        items: String = "",
        message: String? = "",
        isTransfer: Boolean = false
    ) {
        val sb = StringBuilder()
        if (!message.isNullOrEmpty()) sb.append(message)
        else {
            sb.append(
                "Se han ${
                    when (!isTransfer) {
                        false -> "insertado"
                        true -> "transferido"
                    }
                }: ${count.toPadStart()} ${
                    when (count == 1) {
                        true -> item
                        false -> items
                    }
                }"
            )
        }
        Toast.makeText(app, sb.toString(), Toast.LENGTH_LONG).show()
    }

    protected fun getMessage(ex: Exception?) {
        if (ex == null) return
        Toast.makeText(app, ex.message, Toast.LENGTH_LONG).show()
    }

    protected fun repeatUtil(count: Int, prefix: String? = null): List<RepeatAutoFill> {
        val data: MutableList<RepeatAutoFill> = mutableListOf()
        repeat(count) {
            data.add(RepeatAutoFill(it, prefix))
        }
        return data
    }


    //endregion

    //region map
    protected fun <TEntity> getMap(list: List<TEntity>): List<Pair<Int, TEntity>>? {
        if (!list.any()) return null
        val data: MutableList<Pair<Int, TEntity>> = mutableListOf()
        var i = 0
        list.forEach { data.add(Pair(i++, it)) }
        return data
    }

    protected fun <TEntity> getRandomMaxForList(list: List<Pair<Int, TEntity>>): Int =
        when (list.any()) {
            true -> when (list.count() == 1) {
                false -> Random.nextInt(1, list.count())
                true -> 1
            }
            false -> 0
        }

    protected fun <TEntity> getRandomForList(list: List<Pair<Int, TEntity>>): Int =
        when (list.any()) {
            true -> when (list.count() == 1) {
                false -> Random.nextInt(0, (list.count() - 1))
                true -> 0
            }
            false -> 0
        }

    protected fun <TEntity> getRandomEntity(list: List<Pair<Int, TEntity>>): List<TEntity> where TEntity : IEntity {
        val count = getRandomMaxForList(list)
        val data: MutableList<TEntity> = mutableListOf()
        do {
            val temp: TEntity? =
                list.firstOrNull { it.first == getRandomForList(list) }?.second
            if (temp != null && !data.any { it.id == temp.id }) data.add(temp)
        } while (data.count() < count)
        return data
    }
    //endregion

    //region random
    protected fun Int.toRandom(value: Int): Int = when (value <= this) {
        true -> Random.nextInt(this, (this + 1))
        false -> Random.nextInt(this, value)

    }

    protected fun Double.toRandom(value: Double): Double = when (value <= this) {
        true -> Random.nextDouble(this, (this + 1))
        false -> Random.nextDouble(this, value)

    }
    //endregion

    companion object {
        const val IMAGEN_SIZE: Int = 600
    }
}