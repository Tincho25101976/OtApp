package com.vsg.helper.common.util.viewModel.util

import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

class FilterMemberInclude(val entity: KType? = null, val list: KType? = null) {
    private val include: MutableList<FilterMemberInclude> = mutableListOf()

    fun addEntity(type: KType) {
        if (include.any { it.entity == type }) return
        include.add(FilterMemberInclude(entity = type))
    }

    fun addList(type: KType) {
        if (include.any { it.list == type }) return
        include.add(FilterMemberInclude(list = type))
    }

    val includes: List<FilterMemberInclude>
        get() = include
    val isIncludes: Boolean
        get() = include.any()

    val javaTypeEntity: Type?
        get() = when (entity == null) {
            false -> entity.javaType
            true -> null
        }
    val javaTypeList: Type?
        get() = when (list == null) {
            false -> list.javaType
            true -> null
        }


    val isEntity: Boolean
        get() = entity != null
    val isList: Boolean
        get() = list != null
}