package com.vsg.helper.common.util.dao

interface IDaoAllSimpleListRelation<TEntity> {
    fun viewAllSimpleList(idRelation: Long): List<TEntity>?
}