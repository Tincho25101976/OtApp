package com.vsg.helper.common.util.viewModel.util

import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.common.model.EntityForeignKeyList
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import java.lang.reflect.Type
import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaType

class MappingMembers(private val map: KCallable<*>) {

    //region Annotation
    var notationEntityID: EntityForeignKeyID? = null
    var notationEntityList: EntityForeignKeyList? = null
    val isEntityId: Boolean
        get() = notationEntityID != null
    val isEntityList: Boolean
        get() = notationEntityList != null
    //endregion

    //region Callable
    val valueId: Int
        get() = when (isEntityId) {
            true -> notationEntityID!!.id
            false -> 0
        }
    val name: String
        get() = map.name

    var isAccessible: Boolean
        get() = map.isAccessible
        set(value) {
            map.isAccessible = value
        }
//    val type: KType //Class<*>
//        get() = map.returnType

    //get() = map.returnType.javaType as Class<*>
    val kType: KType
        get() = map.returnType

    //    val javaType: Type
//        get() = type.javaType
    val javaKType: Type
        get() = kType.javaType

    val isLong: Boolean
        get() = kType.javaType == Long::class.createType().javaType  //typeOf<Long>() //Long::class.java

    fun <TEntity> toLong(data: TEntity): Long {
        return when (isLong) {
            true -> (map as KProperty1<TEntity, *>).get(data) as Long
            false -> 0L
        }
    }

    val isInt: Boolean
        get() = kType.javaType == Int::class.createType().javaType

    fun <TEntity> toInt(data: TEntity): Int {
        return when (isInt) {
            true -> (map as KProperty1<TEntity, *>).get(data) as Int
            false -> 0
        }
    }

    fun <TEntity> setterEntity(data: TEntity, value: IEntity?) {
        setter(data, value)
    }

    fun <TEntity> setterEntity(data: TEntity, vm: IViewModelView<*>?) {
        setterEntity(data, getValue(vm))
    }

    fun <TEntity> setterListEntity(data: TEntity, value: List<IEntity>?) {
        setter(data, value)
    }

    private fun <TEntity> setter(data: TEntity, value: Any?) {
        try {
            map.isAccessible = true
            (map as KMutableProperty<*>).setter.call(
                data,
                value
            )
        } catch (e: Exception) {

        }
    }
    //endregion

    //region GroupMappingInclude
    private var groupMappingInclude: GroupMappingInclude? = null
    fun setGroupMappingInclude(value: GroupMappingInclude?) {
        this.groupMappingInclude = value
    }

    val isGroupMappingInclude: Boolean
        get() = groupMappingInclude != null
    val typeGroupMappingInclude: KType?  //Class<*>?
        get() = when (isGroupMappingInclude) {
            true -> groupMappingInclude!!.type
            false -> null
        }
    //endregion

    //region value
    fun getValue(vm: IViewModelView<*>?): IEntity? {
        if (vm == null || !isGroupMappingInclude) return null
        return vm.viewModelView(groupMappingInclude!!.idEntity)
    }

    fun getValue(id: Int, vm: IViewModelAllSimpleListIdRelation<*>?): List<IEntity>? {
        if (vm == null || !isGroupMappingInclude) return null
        return vm.viewModelViewAllSimpleList(id)
    }
    //endregion
}