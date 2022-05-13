package com.vsg.helper.ui.data

import com.vsg.helper.common.model.IEntity

interface IReadToList<T> where T : IEntity {
    fun readToList(): MutableList<T>
}