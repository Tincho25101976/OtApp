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
import com.vsg.helper.common.util.viewModel.IViewModelAllTextSearch
import com.vsg.helper.helper.file.HelperFile.Static.sendFile
import com.vsg.helper.ui.adapter.IDataAdapterEnum
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.export.*
import com.vsg.helper.ui.popup.action.UICustomAlertDialogActionParameter
import com.vsg.helper.ui.popup.export.UICustomAlertDialogExport
import com.vsg.helper.ui.popup.export.UICustomAlertDialogExportParameter
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

    //region properties
//    private lateinit var actionExport: UICustomAlertDialogExport<TActivity, TEntity>
    //endregion

    //region method
//    init {
//        onEventSetUICustomAlertDialogActionType = { UICustomAlertDialogActionParameter() }
//    }

    protected fun sendReport(e: TEntity, type: ExportType) {
        val directory: String = Environment.DIRECTORY_DOCUMENTS
        val iExport: IUIEntityToFile<TEntity> = when (type) {
            ExportType.PDF -> UIReportFormatPDF()
            else -> null
        } ?: return
        this.sendFile(iExport.toFile(e, this, directory))
    }
    //endregion
}