package com.vsg.helper.common.cast

import androidx.room.TypeConverter
import com.vsg.helper.helper.Helper.Companion.toDate
import com.vsg.helper.helper.date.HelperDate.Companion.toLong
import java.util.*


class Convert {
    //region date <-> long
    @TypeConverter
    fun dateToLong(data: Date?): Long? = data.toLong()

    @TypeConverter
    fun longToDate(data: Long?): Date? = data.toDate()
    //endregion
}