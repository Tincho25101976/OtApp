package com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.vsg.utilities.common.util.dao.IDaoAllUpdateIsDefault
import com.vsg.utilities.common.util.dao.IGenericDaoPagingRelationCode
import common.model.master.section.MasterSection

@Dao
abstract class SectionDao : IGenericDaoPagingRelationCode<MasterSection>, IDaoAllUpdateIsDefault {
    //region paging
    @Query("SELECT * FROM ${MasterSection.ENTITY_NAME} WHERE idWarehouse = :idRelation ORDER BY valueCode")
    abstract override fun viewAllPaging(idRelation: Long): DataSource.Factory<Int, MasterSection>
    //endregion

    //region code
    @Query("SELECT IFNULL(MAX(number), 0) + 1 FROM ${MasterSection.ENTITY_NAME} WHERE idWarehouse = :idRelation")
    abstract override fun viewNextAutoCode(idRelation: Long): Long
    //endregion

    //region items
    @Query("SELECT * FROM ${MasterSection.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Long): MasterSection?
    //endregion

    //region enabled
    @Query("UPDATE ${MasterSection.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Long)
    //endregion

    //region search
    @Query("SELECT valueCode FROM ${MasterSection.ENTITY_NAME} WHERE idWarehouse = :idRelation GROUP BY valueCode ORDER BY valueCode")
    abstract override fun viewGetAllTextSearch(idRelation: Long): LiveData<List<String>>
    //endregion

    //region parameters
    @Query("SELECT EXISTS(SELECT * FROM ${MasterSection.ENTITY_NAME} WHERE idWarehouse = :idRelation)")
    abstract override fun viewHasItems(idRelation: Long): Boolean
    //endregion

    //region list
    @Query("SELECT * FROM ${MasterSection.ENTITY_NAME} WHERE idWarehouse = :idRelation ORDER BY valueCode")
    abstract override fun viewAllSimpleList(idRelation: Long): List<MasterSection>?
    @Query("SELECT * FROM ${MasterSection.ENTITY_NAME} WHERE idCompany = :idRelation ORDER BY valueCode")
    abstract fun viewAllSimpleListByCompany(idRelation: Long): List<MasterSection>?
    @Query("SELECT * FROM ${MasterSection.ENTITY_NAME} ORDER BY valueCode")
    abstract fun viewAllSimpleList(): List<MasterSection>?
    //endregion

    //region disabled
    @Query("SELECT * FROM ${MasterSection.ENTITY_NAME} WHERE id < 0")
    abstract override fun viewAll(): LiveData<List<MasterSection>>?
    //endregion

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${MasterSection.ENTITY_NAME} WHERE id = :entity)")
    abstract override fun checkExitsEntity(entity: Long): Boolean
    //endregion

    //region updateIsDefault
    @Transaction
    @Query("UPDATE ${MasterSection.ENTITY_NAME} SET isDefault = CASE id WHEN :idEntity THEN 1 ELSE 0 END WHERE idWarehouse = :idRelation ")
    abstract override fun updateSetAllIsDefault(idEntity: Long, idRelation: Long)
    //endregion
}