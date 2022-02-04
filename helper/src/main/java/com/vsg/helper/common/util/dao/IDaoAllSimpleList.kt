package com.vsg.helper.common.util.dao

interface IDaoAllSimpleList<TEntity> {
    fun viewAllSimpleList(): List<TEntity>?
}