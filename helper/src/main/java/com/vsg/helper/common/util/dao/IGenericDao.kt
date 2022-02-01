package com.vsg.helper.common.util.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IIsEnabled

interface IGenericDao<TEntity> :
    IGenericDaoUpdateEnabled<TEntity>,
    IDaoCheckExitsEntity
        where TEntity : IEntity,
              TEntity : IIsEnabled {
    @Insert
    suspend fun insert(item: TEntity): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: TEntity): Int

    @Delete
    suspend fun delete(item: TEntity): Int

    fun viewAll(): LiveData<List<TEntity>>?
    fun view(id: Int): TEntity?
}