package com.vsg.helper.ui.util

import android.os.Environment
import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.common.export.IEntityExport
import com.vsg.helper.common.model.*
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.helper.common.report.IEntityReport
import com.vsg.helper.common.util.dao.IDaoAllTextSearch
import com.vsg.helper.common.util.dao.IGenericDaoPagingParse
import com.vsg.helper.common.util.viewModel.IViewModelAllPaging
import com.vsg.helper.common.util.viewModel.IViewModelAllSimpleListWithRelation
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.helper.file.HelperFile.Static.sendFile
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.export.IUIEntityToFile
import com.vsg.helper.ui.report.pdf.UIReportFormatPDF

@ExperimentalStdlibApi
abstract class CurrentBaseActivityPagingGenericParseExportReport<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud>(
    type: Class<TViewModel>,
    typeFilter: Class<TFilter>
) :
    CurrentBaseActivityPagingGenericParseExport<TActivity, TViewModel, TDao, TEntity, TFilter, TCrud>(
        type,
        typeFilter
    )
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : IViewModelAllPaging<TEntity>,
              TViewModel : IViewModelAllTextSearch,
              TViewModel : ViewModelGenericParse<TDao, TEntity>,
              TViewModel : IViewModelAllSimpleListWithRelation<TEntity>,
              TDao : IGenericDaoPagingParse<TEntity>,
              TDao : IDaoAllTextSearch,
              TEntity : IEntity,
              TEntity : IEntityExport,
              TEntity : IEntityReport,
              TEntity : IEntityParse<TEntity>,
              TEntity : IDataAdapter,
              TEntity : IIsEnabled,
              TEntity : IEntityCreateDate,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : Comparable<TEntity>,
              TFilter : IDataAdapterEnum,
              TFilter : Enum<TFilter>,
              TEntity : ItemBase,
              TCrud : UICustomCRUDViewModel<TActivity, TViewModel, TDao, TEntity> {

    //region event
    protected var onSetTitleReport: ((Unit) -> String)? = null
    //endregion

    //region method
    protected fun sendReport(e: TEntity, type: ExportType) {
        val data = this.currentViewModel().viewModelViewWithRelations(e.id) ?: return
        val directory: String = Environment.DIRECTORY_DOCUMENTS
        val iExport: IUIEntityToFile<TEntity> = when (type) {
            ExportType.PDF -> UIReportFormatPDF<TEntity>().apply {
                this.onEventSetTitle = {
                    this@CurrentBaseActivityPagingGenericParseExportReport.onSetTitleReport?.invoke(
                        Unit
                    ) ?: ""
                }
            }
            else -> null
        } ?: return
        this.sendFile(iExport.toFile(data, this, directory))
    }
    //endregion
}