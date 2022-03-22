package com.vsg.helper.common.util.viewModel.util

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IDaoCheckExitsEntity
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.viewModel.MakeGenericViewModel
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

class DataMakeViewModelView<TDao, TEntity>(
    val type: KType,
    val viewModel: MakeGenericViewModel<TDao, TEntity>

) where TDao : IGenericDao<TEntity>,
        TDao : IDaoCheckExitsEntity,
        TEntity : IEntity,
        TEntity : IIsEnabled {

    fun isType(t: KType): Boolean {
        if(type.javaType == t.javaType) return true
        return try {
            val temp = t.jvmErasure == List::class && t.arguments[0].type?.javaType == this.type.javaType
            temp
        }
        catch (ex: Exception){
            false
        }
    }
}