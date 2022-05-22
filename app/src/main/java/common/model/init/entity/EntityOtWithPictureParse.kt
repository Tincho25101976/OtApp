package com.vsg.ot.common.model.init.entity

import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IEntityParse
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.helper.helper.date.HelperDate.Companion.toDate
import com.vsg.helper.helper.string.HelperString.Static.toBool
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import java.util.*
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

abstract class EntityOtWithPictureParse<T> :
    EntityOtWithPicture<T>(), IEntityParse<T>, IDataAdapter
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {

    //region properties
    override val tag: String
        get() = javaClass.simpleName

    override fun titleAdapter(): String? = title
    override fun bodyAdapter(): String = description
    //endregion

    //region methods
    abstract override fun getFields(): List<String>

    abstract fun aGetItemCast(): T
    override fun cast(s: HashMap<String?, String?>): T {
        val data = XactSector::class.memberProperties
            .filter { s.keys.contains(it.name) }
            .map { it }
        val result = aGetItemCast()
        if (!data.any()) return result
        data.filterIsInstance<KMutableProperty<*>>().forEach {
            val value = s[it.name] ?: ""
            when (it.returnType) {
                Int::class.starProjectedType -> it.setter.call(result, value.toInt())
                Double::class.starProjectedType -> it.setter.call(result, value.toDouble())
                String::class.starProjectedType -> it.setter.call(result, value)
                Long::class.starProjectedType -> it.setter.call(result, value.toLong())
                Date::class.starProjectedType -> it.setter.call(result, value.toDate())
                Boolean::class.starProjectedType -> it.setter.call(result, value.toBool())
            }
        }
        return result
    }
    //endregion
}