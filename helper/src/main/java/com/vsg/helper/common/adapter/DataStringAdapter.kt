package com.vsg.helper.common.adapter

class DataStringAdapter<T>(var data: String, var tag: ((T) -> Boolean)? = null) {
    suspend fun query(): ((T) -> Boolean)? = tag
}