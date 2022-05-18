package com.vsg.ot.common.model.securityDialog.xact.sector

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.init.entity.EntityOtParse

@Entity(
    indices = [
        Index(
            value = arrayOf("valueCode", "description"),
            name = "IX_XACT_SECTOR"
        )],
    inheritSuperIndices = true,
    tableName = XactSector.ENTITY_NAME
)
class XactSector : EntityOtParse<XactSector>() {

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_xact_sector

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(valueCode)

    override fun aEquals(other: Any?): Boolean {
        if (other !is XactSector) return false
        return valueCode == other.valueCode
    }

    override fun getFields(): List<String> {
        return listOf("id", "valueCode", "description", "isDefault", "isEnabled")
    }

    override fun aGetItemCast(): XactSector = XactSector()
    //endregion

    companion object {
        const val ENTITY_NAME = "masterXactSector"
    }

//    override fun getFields(): List<String> {
//        return listOf("id", "valueCode", "description", "isDefault", "isEnabled")
//    }
//
//    override fun cast(s: HashMap<String?, String?>): XactSector {
//        val data = XactSector::class.memberProperties
//            .filter { s.keys.contains(it.name) }
//            .map { it }
//        val result = XactSector()
//        if (!data.any()) return result
//        data.filterIsInstance<KMutableProperty<*>>().forEach {
//            val value = s[it.name] ?: ""
//            when (it.returnType) {
//                Int::class.starProjectedType -> it.setter.call(result, value.toInt())
//                Double::class.starProjectedType -> it.setter.call(result, value.toDouble())
//                String::class.starProjectedType -> it.setter.call(result, value)
//                Long::class.starProjectedType -> it.setter.call(result, value.toLong())
//                Date::class.starProjectedType -> it.setter.call(result, value.toDate())
//                Boolean::class.starProjectedType -> it.setter.call(result, value.toBool())
//            }
//        }
//        return result
//    }
}