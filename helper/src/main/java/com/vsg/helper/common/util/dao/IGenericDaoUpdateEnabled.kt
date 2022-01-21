package com.vsg.helper.common.util.dao

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsEnabled


interface IGenericDaoUpdateEnabled<T> where T : IEntity, T : IIsEnabled {
    fun updateSetEnabled(data: Long)
}