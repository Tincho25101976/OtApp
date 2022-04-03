package common.model.master.stock

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import common.model.init.dao.DaoGenericOtCompany
import common.model.master.batch.MasterBatch
import common.model.master.item.MasterItem

@Dao
abstract class MasterStockDao : DaoGenericOtCompany<MasterStock>() {
    //region paging
    @Query("SELECT * FROM ${MasterStock.ENTITY_NAME} ORDER BY createDate")
    abstract override fun viewAllPaging(): DataSource.Factory<Int, MasterStock>

    @Query("SELECT * FROM ${MasterStock.ENTITY_NAME} WHERE idCompany = :idRelation ORDER BY createDate")
    abstract override fun viewAllPaging(idRelation: Int): DataSource.Factory<Int, MasterStock>
    //endregion

    @Query("SELECT * FROM ${MasterStock.ENTITY_NAME} WHERE id = :id LIMIT 1")
    abstract override fun view(id: Int): MasterStock?

    @Query("SELECT * FROM ${MasterStock.ENTITY_NAME} ORDER BY createDate")
    abstract override fun viewAll(): LiveData<List<MasterStock>>?

    @Query("SELECT * FROM ${MasterStock.ENTITY_NAME} ORDER BY createDate")
    abstract override fun viewAllSimpleList(): List<MasterStock>?

    @Query("UPDATE ${MasterStock.ENTITY_NAME} SET isEnabled = NOT isEnabled WHERE id = :data")
    abstract override fun updateSetEnabled(data: Int)

    @Query("SELECT description FROM ${MasterStock.ENTITY_NAME} WHERE idCompany = :idRelation GROUP BY description ORDER BY description")
    abstract override fun viewGetAllTextSearch(idRelation: Int): LiveData<List<String>>

    @Query("SELECT EXISTS(SELECT * FROM ${MasterStock.ENTITY_NAME} WHERE idCompany = :idRelation)")
    abstract override fun viewHasItems(idRelation: Int): Boolean

    @Query("SELECT * FROM ${MasterStock.ENTITY_NAME} WHERE idCompany = :idRelation ORDER BY description")
    abstract override fun viewAllSimpleList(idRelation: Int): List<MasterStock>?

    //region check
    @Query("SELECT EXISTS(SELECT * FROM ${MasterStock.ENTITY_NAME} WHERE id = :id)")
    abstract override fun checkExitsEntity(id: Int): Boolean
    //endregion

    //region util
    @Query(
        "SELECT " +
                "st.* " +
                "FROM ${MasterStock.ENTITY_NAME} st " +
                "INNER JOIN ${MasterBatch.ENTITY_NAME} bt " +
                "   ON bt.id = st.idBatch AND bt.idCompany = st.idCompany " +
                "INNER JOIN ${MasterItem.ENTITY_NAME} p " +
                "   ON p.id = st.idItem AND p.idCompany = st.idCompany AND bt.idItem = p.id " +
                "WHERE st.idCompany = :idRelation " +
                "       AND DATE('now', 'localtime') <= bt.dueDate " +
                "       AND p.shellLife >= JulianDay(bt.dueDate) - JulianDay(DATE('now', 'localtime')) " +
        "ORDER BY description"
    )
    abstract fun getNearExpiredByCompany(idRelation: Int): List<MasterStock>?
    @Query(
        "SELECT " +
                "st.* " +
                "FROM ${MasterStock.ENTITY_NAME} st " +
                "INNER JOIN ${MasterBatch.ENTITY_NAME} bt " +
                "   ON bt.id = st.idBatch AND bt.idCompany = st.idCompany " +
                "INNER JOIN ${MasterItem.ENTITY_NAME} p " +
                "   ON p.id = st.idItem AND p.idCompany = st.idCompany AND bt.idItem = p.id " +
                "WHERE st.idItem = :idRelation " +
                "       AND DATE('now', 'localtime') <= bt.dueDate " +
                "       AND p.shellLife >= JulianDay(bt.dueDate) - JulianDay(DATE('now', 'localtime')) " +
                "ORDER BY description"
    )
    abstract fun getNearExpiredByItem(idRelation: Int): List<MasterStock>?
    @Query(
        "SELECT " +
                "st.* " +
                "FROM ${MasterStock.ENTITY_NAME} st " +
                "INNER JOIN ${MasterBatch.ENTITY_NAME} bt " +
                "   ON bt.id = st.idBatch AND bt.idCompany = st.idCompany " +
                "INNER JOIN ${MasterItem.ENTITY_NAME} p " +
                "   ON p.id = st.idItem AND p.idCompany = st.idCompany AND bt.idItem = p.id " +
                "WHERE st.idSection = :idRelation " +
                "       AND DATE('now', 'localtime') <= bt.dueDate " +
                "       AND p.shellLife >= JulianDay(bt.dueDate) - JulianDay(DATE('now', 'localtime')) " +
                "ORDER BY description"
    )
    abstract fun getNearExpiredBySection(idRelation: Int): List<MasterStock>?
    @Query(
        "SELECT " +
                "st.* " +
                "FROM ${MasterStock.ENTITY_NAME} st " +
                "INNER JOIN ${MasterBatch.ENTITY_NAME} bt " +
                "   ON bt.id = st.idBatch AND bt.idCompany = st.idCompany " +
                "INNER JOIN ${MasterItem.ENTITY_NAME} p " +
                "   ON p.id = st.idItem AND p.idCompany = st.idCompany AND bt.idItem = p.id " +
                "WHERE st.idWarehouse = :idRelation " +
                "       AND DATE('now', 'localtime') <= bt.dueDate " +
                "       AND p.shellLife >= JulianDay(bt.dueDate) - JulianDay(DATE('now', 'localtime')) " +
                "ORDER BY description"
    )
    abstract fun getNearExpiredByWarehouse(idRelation: Int): List<MasterStock>?
    //endregion
}