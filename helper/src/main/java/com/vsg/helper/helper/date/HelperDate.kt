package com.vsg.helper.helper.date

import android.annotation.SuppressLint
import com.vsg.helper.common.format.FormatDateString
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import java.util.concurrent.TimeUnit

class HelperDate {
    companion object {
        fun formatDate(): DateFormat = formatDate(FormatDateString.SIMPLE)
        private val mapDayFull: Map<String, String> =
            mapOf(
                "Sunday" to "Domingo",
                "Monday" to "Lunes",
                "Tuesday" to "Martes",
                "Wednesday" to "Miércoles",
                "Thursday" to "Jueves",
                "Friday" to "Viernes",
                "Saturday" to "Sábado"
            )
        private val mapMonthFull: Map<String, String> =
            mapOf(
                "January" to "Enero",
                "February" to "Febrero",
                "March" to "Marzo",
                "April" to "Abril",
                "May" to "Mayo",
                "June" to "Junio",
                "August" to "Agosto",
                "September" to "Septiembre",
                "October" to "Octubre",
                "November" to "Noviembre",
                "December" to "Diciembre"
            )

        @SuppressLint("SimpleDateFormat")
        fun formatDate(format: FormatDateString): DateFormat = SimpleDateFormat(format.data)
        fun String.toDate(): Date? = formatDate().parse(this)
        fun String.toDate(format: FormatDateString): Date? = formatDate(format).parse(this)
        fun Date.toLocalDate(): LocalDate? {
            return this.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        fun LocalDateTime.toDate(): Date? {
            val instant: Instant = this.toInstant(ZoneOffset.UTC)
            return Date.from(instant)
        }

        fun now(): Date = Calendar.getInstance().time
        fun nowDate(): Date = now().date()!!
        fun Date?.date(): Date?{
            if(this == null) return null
            val c = Calendar.getInstance()
            c[Calendar.HOUR_OF_DAY] = 0
            c[Calendar.MINUTE] = 0
            c[Calendar.SECOND] = 0
            c[Calendar.MILLISECOND] = 0
            return c.time
        }
        fun Date?.addDay(value: Int): Date? = this.add(value, TypeAddDate.DAYS)
        fun Date?.addMonth(value: Int): Date? = this.add(value, TypeAddDate.MONTH)
        fun Date?.addSecond(value: Int): Date? = this.add(value, TypeAddDate.SECONDS)
        fun Date?.addMinute(value: Int): Date? = this.add(value, TypeAddDate.MINUTES)
        fun Date?.addHour(value: Int): Date? = this.add(value, TypeAddDate.HOURS)
        fun Date?.addMillisecond(value: Int): Date? = this.add(value, TypeAddDate.MILLISECONDS)
        fun Date?.addYear(value: Int): Date? = this.add(value, TypeAddDate.YEAR)
        private fun Date?.add(value: Int, type: TypeAddDate): Date? {
            if (this == null) return null
            val c: Calendar = Calendar.getInstance()
            c.time = this
            when (type) {
                TypeAddDate.DAYS -> c.add(Calendar.DATE, value)
                TypeAddDate.HOURS -> c.add(Calendar.HOUR, value)
                TypeAddDate.MINUTES -> c.add(Calendar.MINUTE, value)
                TypeAddDate.SECONDS -> c.add(Calendar.SECOND, value)
                TypeAddDate.MILLISECONDS -> c.add(Calendar.MILLISECOND, value)
                TypeAddDate.MONTH -> c.add(Calendar.MONTH, value)
                TypeAddDate.YEAR -> c.add(Calendar.YEAR, value)
            }
            return c.time
        }

        fun Date?.toShortDateString(): String = when (this == null) {
            true -> ""
            false -> formatDate().format(this)
        }

        fun Date?.toDateString(format: FormatDateString): String {
            if (this == null) return ""
            return when (format) {
                FormatDateString.SIMPLE -> formatDate(format).format(this)
                FormatDateString.COMPLETO -> formatDate(format).format(this)
                FormatDateString.CREATE_DATE -> formatDate(format).format(this)
                FormatDateString.AUDIT -> formatDate(format).format(this)
                FormatDateString.FULL -> {
                    var temp: String = formatDate(format).format(this)
                    mapDayFull.filter { temp.contains(it.key) }.forEach {
                        temp = temp.replace(it.key, it.value, true)
                    }
                    mapMonthFull.filter { temp.contains(it.key) }.forEach {
                        temp = temp.replace(it.key, it.value, true)
                    }
                    when (temp.isEmpty()) {
                        true -> formatDate(format).format(this)
                        false -> temp
                    }
                }
                FormatDateString.FILE -> formatDate(format).format(this)
            }
        }

        fun Date?.getAge(): Int {
            if (this == null) return -1
            val dobCalendar = Calendar.getInstance()
            dobCalendar.time = this
            val today = Calendar.getInstance()
            var age = today[Calendar.YEAR] - dobCalendar[Calendar.YEAR]
            if (today[Calendar.MONTH] == dobCalendar[Calendar.MONTH]) {
                if (today[Calendar.DAY_OF_MONTH] < dobCalendar[Calendar.DAY_OF_MONTH]) {
                    age--
                }
            } else if (today[Calendar.MONTH] < dobCalendar[Calendar.MONTH]) {
                age--
            }
            return age
        }

        fun Date?.toLong(): Long? {
            return when (this == null) {
                true -> null
                false -> this.time
            }
        }

        //region periodo
        fun Date?.toPeriod(to: Date?, type: TypePeriodDate = TypePeriodDate.DAYS): Long {
            if (this == null || to == null) return 0L
            val status: Long = when (this < to) {
                true -> 1L
                else -> -1L
            }
            val value: Long = to.time - this.time
            val result = when (type) {
                TypePeriodDate.DAYS -> TimeUnit.MILLISECONDS.toDays(value)
                TypePeriodDate.HOURS -> TimeUnit.MILLISECONDS.toHours(value)
                TypePeriodDate.MINUTES -> TimeUnit.MILLISECONDS.toMinutes(value)
                TypePeriodDate.SECONDS -> TimeUnit.MILLISECONDS.toSeconds(value)
                TypePeriodDate.MILLISECONDS -> TimeUnit.MILLISECONDS.toMillis(value)
            }
            return result * status
        }
        //endregion
    }

    enum class TypePeriodDate {
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
        MILLISECONDS
    }

    enum class TypeAddDate {
        DAYS,
        HOURS,
        MINUTES,
        SECONDS,
        MILLISECONDS,
        MONTH,
        YEAR
    }
}