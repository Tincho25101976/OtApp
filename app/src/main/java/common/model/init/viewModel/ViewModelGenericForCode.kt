package common.model.init.viewModel

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IIsDefault
import com.vsg.helper.common.model.IIsEnabled
import com.vsg.helper.common.util.dao.IDaoAllSimpleListRelation
import com.vsg.helper.common.util.dao.IDaoAllTextSearchRelation
import com.vsg.helper.common.util.dao.IGenericDao
import com.vsg.helper.common.util.dao.IGenericDaoPagingRelation
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListIdRelation
import com.vsg.helper.common.util.viewModel.IViewModelView
import com.vsg.helper.common.util.viewModel.MakeGenericViewModelPagingRelation
import common.model.master.batch.BatchViewModel
import common.model.master.batch.MasterBatch
import common.model.master.company.CompanyViewModel
import common.model.master.company.MasterCompany
import common.model.master.item.MasterItem
import common.model.master.item.ProductViewModel
import common.model.master.section.MasterSection
import common.model.master.section.SectionViewModel
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.WarehouseViewModel
import kotlin.reflect.KType

@ExperimentalStdlibApi
abstract class ViewModelGenericForCode<TDao, TEntity>(dao: TDao, context: Application) :
    MakeGenericViewModelPagingRelation<TDao, TEntity>(
        dao, context
    )
        where TDao : IGenericDao<TEntity>,
              TDao : IGenericDaoPagingRelation<TEntity>,
              TDao : IDaoAllTextSearchRelation,
              TDao : IDaoAllSimpleListRelation<TEntity>,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IIsDefault,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity> {


    protected fun dataForTransaction(): AppDatabase = AppDatabase.getInstance(context)!!

    //region checked
    fun isEntityCheck(entity: IEntity?): Boolean {
        if (entity == null) return false
        return when (entity) {
            is MasterItem -> ProductViewModel(context).isEntity(entity)
            is MasterCompany -> CompanyViewModel(context).isEntity(entity)
            is MasterBatch -> BatchViewModel(context).isEntity(entity)
            is MasterWarehouse -> WarehouseViewModel(context).isEntity(entity)
            is MasterSection -> SectionViewModel(context).isEntity(entity)

            else -> false
        }
    }

    fun checkExistsEntityCheck(entity: IEntity?): Boolean {
        if (entity == null) return false
        return when (entity) {
            is MasterItem -> ProductViewModel(context).checkExistsEntity(entity)
            is MasterCompany -> CompanyViewModel(context).checkExistsEntity(entity)
            is MasterBatch -> BatchViewModel(context).checkExistsEntity(entity)
            is MasterWarehouse -> WarehouseViewModel(context).checkExistsEntity(entity)
            is MasterSection -> SectionViewModel(context).checkExistsEntity(entity)

            else -> false
        }
    }
    //endregion

    //region IViewModel
    override fun getInstanceOfIViewModelView(type: KType): IViewModelView<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelView(type, context)
    }

    override fun getInstanceOfIViewModelAllSimpleListIdRelation(type: KType): IViewModelAllSimpleListIdRelation<*>? {
        return ViewModelStoredMap.getInstanceOfIViewModelAllSimpleListIdRelation(type, context)
    }
    //endregion
}