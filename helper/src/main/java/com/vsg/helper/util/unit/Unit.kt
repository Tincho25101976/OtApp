package com.vsg.helper.util.unit

import com.vsg.helper.ui.adapter.IDataAdapterTitle
import com.vsg.helper.util.helper.HelperUtil.Companion.toUnit
import com.vsg.helper.util.unit.type.TypeUnit
import com.vsg.helper.util.unit.type.TypeUnit.Companion.toUnitDetail

class Unit(val unit: TypeUnit) : IDataAdapterTitle {
    //region properties
    val symbol: String get() = unit.toUnitDetail()
    val order: Int get() = unit.order
    val value: Int get() = unit.value

    override val title: String
        get() = unit.title
    //endregion

//    override fun oGetDrawablePicture(): Int = R.drawable.pic_unit
//    override fun oGetSpannedGeneric(): StringBuilder =
//        StringBuilder().toTitleSpanned(title)
//            .toLineSpanned("Símbolo", symbol)
//
//    override fun aEquals(other: Any?): Boolean {
//        if (other !is Unit) return false
//        return unit == other.unit
//    }
    //

    companion object {
        const val DEFAULT_VALUE_PRECISION: Int = 3

        fun getDefaultUnit(): Unit = TypeUnit.UNDEFINED.toUnit()
    }
}