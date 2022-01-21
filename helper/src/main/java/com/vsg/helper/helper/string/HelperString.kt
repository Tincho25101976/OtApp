package com.vsg.helper.helper.string

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Html
import android.text.Spanned
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class HelperString {
    companion object Static {
        private const val CHAR_NEW_LINE: Char = '\n'
        private const val START_TEXT: Char = 2.toChar()
        private const val END_TEXT: Char = 3.toChar()
        private const val TITLE: String = "h1"
        private const val NEW_LINE: String = "<br>"

        //region html
        private fun String.toTagStart() = "<$this>"
        private fun String.toTagEnd() = "</$this>"
        private fun map(): Map<Char, String> {
            return mapOf(
                START_TEXT to TITLE.toTagStart(),
                END_TEXT to TITLE.toTagEnd(),
                '\n' to NEW_LINE
            )
        }

        fun defaultSpanned(): Spanned = Html.fromHtml("", Html.FROM_HTML_MODE_COMPACT)

        fun String.toTitle(): String = when (this.isEmpty()) {
            true -> ""
            false -> StringBuilder().append(START_TEXT).append(this).append(END_TEXT).toString()
        }

        fun StringBuilder.toHtml(): Spanned {
            if (this.isEmpty()) return defaultSpanned()
            val map = map()
            val result = StringBuilder()
            this.forEach {
                if (map.any { s -> s.key == it }) result.append(map[it])
                else result.append(it)
            }
            return result.toString().castToHtml()
        }

        fun String.toHtml(): Spanned {
            if (this.isEmpty()) return defaultSpanned()
            val map = map()
            val result = StringBuilder()
            this.forEach {
                if (map.any { s -> s.key == it }) result.append(map[it])
                else result.append(it)
            }
            return result.toString().castToHtml()
        }

        fun String.castToHtml(): Spanned {
            if (this.isEmpty()) return defaultSpanned()
            return Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
        }

        fun StringBuilder.castToHtml(): Spanned {
            if (this.isEmpty()) return defaultSpanned()
            val result = StringBuilder()
            this.forEach {
                if (it == CHAR_NEW_LINE) result.append(TypeTagHTML.BR.data.toTagStart())
                else result.append(it)
            }
            return result.toString().castToHtml()
        }

        fun String.toTag(tag: TypeTagHTML): String {
            if (tag == TypeTagHTML.BR) return StringBuilder().append(this)
                .append(tag.data.toTagStart()).toString()
            return StringBuilder().append(tag.data.toTagStart()).append(this)
                .append(tag.data.toTagEnd()).toString()
        }

        fun String.toTag(vararg tag: TypeTagHTML): String {
            if (tag.isEmpty()) return ""
            val tags = tag.toList().groupBy { it }.map { it.key }
            if (!tags.any()) return ""
            val result = StringBuilder().append(this)
            tags.forEach {
                if (it != TypeTagHTML.BR) {
                    result.insert(0, it.data.toTagStart())
                    result.append(it.data.toTagEnd())
                }
            }
            if (tags.any { it == TypeTagHTML.BR }) result.append(TypeTagHTML.BR.data.toTagStart())
                .toString()
            return result.toString()
        }

        private fun String.formatCaption(): String =
            StringBuilder().append(this).append(": ").toString()

        private fun String.toTagBold() = this.toTag(TypeTagHTML.B)
        fun StringBuilder.toTagBold(text: String) = this.append(text.toTag(TypeTagHTML.B))
        private fun String.toTagCaption(underline: Boolean = true) = when (underline) {
            true -> this.toTag(TypeTagHTML.B, TypeTagHTML.U)
            false -> this.toTag(TypeTagHTML.B)
        }

        private fun String.toTagTitle() = this.toTag(TypeTagHTML.H2)
        fun StringBuilder.toLineSpanned(caption: String, item: String?, underline: Boolean = true) =
            this.append(caption.formatCaption().toTagCaption(underline)).append(item).appendLine()

        fun StringBuilder.toOneLineSpanned(
            caption: String,
            item: String?,
            underline: Boolean = true
        ) =
            this.append(caption.formatCaption().toTagCaption(underline)).append(item)

        fun StringBuilder.toTitleSpanned(caption: String) =
            this.append(caption.formatCaption().toTagTitle())

        fun String.toTitleSpanned(puntos: Boolean = true): Spanned = when (puntos) {
            true -> StringBuilder().append(this.formatCaption().toTagTitle()).castToHtml()
            false -> StringBuilder().append(this.toTagTitle()).castToHtml()
        }
        //endregion

        //region clipboard
        fun Activity.toClipBoard(text: String, label: String = "") {
            val clipboard: ClipboardManager? =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText(label, text)
            clipboard?.setPrimaryClip(clip)
        }

        fun Activity.fromClipBoard(): String {
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager? ?: return ""
            var result = ""
            val clipData: ClipData? = clipboard.primaryClip
            clipData?.apply {
                result = this.getItemAt(0).text.toString().trim()
            }
            return result
        }
        //endregion

        //region validate
        private fun String.isCompareRegEx(expression: String): Boolean {
            if (expression.isEmpty() || this.isEmpty()) return false
            val p: Pattern = Pattern.compile(expression)
            val m: Matcher = p.matcher(this)
            return m.find() && m.group() == this
        }

        fun String.isMailAddress(): Boolean {
            val p =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            return this.isCompareRegEx(p)
        }

        fun String.isPhoneNumber(): Boolean {
            val p = "(0/91)?[7-9][0-9]{9}"
            return this.isCompareRegEx(p)
        }

        fun String.isValidatePhoneNumber(): Boolean {
            val phoneNumberUtil = PhoneNumberUtil.getInstance()
            val number: PhoneNumber
            try {
                number = phoneNumberUtil.parse(this, PhoneNumber.CountryCodeSource.UNSPECIFIED.name)
            } catch (e: Exception) {
                return false
            }
            val data1 = phoneNumberUtil.isPossibleNumberForType(number, PhoneNumberType.FIXED_LINE)
            val data2 = phoneNumberUtil.isPossibleNumberForType(number, PhoneNumberType.TOLL_FREE)
            val data3 = phoneNumberUtil.isPossibleNumberForType(number, PhoneNumberType.MOBILE)
            val data4 = phoneNumberUtil.isValidNumber(number)
            val result = data1 || data2 || data3 || data4
            return result
        }

        //endregion

        //region cast
        fun String?.decodeToBitmap(): Bitmap? {
            if (this == null || this.isEmpty()) return null
            return try {
                val encodeByte: ByteArray = Base64.getDecoder().decode(this)
                val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                bitmap
            } catch (e: Exception) {
                null
            }
        }
        //endregion

        //region util
        fun String.capitalizeWords(): String =
            split(" ").joinToString(" ") { it.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT) }

        //        fun String.capitalizeWords(): String = split(" ").map { it.toLowerCase().capitalize() }.joinToString(" ")
        fun String.removeAllSpace(): String = when (this.isEmpty()) {
            true -> this
            false -> this.replace("\\s".toRegex(), "")
        }
        //endregion
    }
}