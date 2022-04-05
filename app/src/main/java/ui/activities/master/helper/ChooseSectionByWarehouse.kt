package com.vsg.agendaandpublication.ui.activities.itemOperation.helper

import android.app.Application
import android.view.View
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.Section
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.Warehouse
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.utilities.ui.widget.spinner.CustomSpinner


@ExperimentalStdlibApi
class ChooseSectionByWarehouse(
    view: View,
    private val app: Application,
    private val company: Company
) {
    //region handler
    var onEventSectionSelected: ((Section?) -> Unit)? = null
    private var onEventPauseCallBackItemSelect: (() -> Section?)? = null
    //endregion

    //region widget
    private var tWarehouse: CustomSpinner
    private var tSection: CustomSpinner = view.findViewById(R.id.SelectSectionByWarehouseSection)
    //endregion

    //region properties
    private var warehouse: Warehouse? = null
    private var section: Section? = null
    //endregion

    //region init
    init {
        tWarehouse =
            view.findViewById<CustomSpinner>(R.id.SelectSectionByWarehouseWarehouse).apply {
                setCustomAdapter(
                    WarehouseViewModel(app).viewModelViewAllSimpleList(company.id),
                    callBackItemSelect = {
                        this@ChooseSectionByWarehouse.warehouse = it
                        if (it != null) {
                            val setItem = onEventPauseCallBackItemSelect?.invoke()
                            tSection.apply {
                                setCustomAdapter(
                                    data = SectionViewModel(app).viewModelViewAllSimpleList(it.id),
                                    callBackItemSelect = { sec ->
                                        this@ChooseSectionByWarehouse.section = sec
                                        onEventSectionSelected?.invoke(sec)
                                    },
                                    selectItem = setItem
                                )
                            }
                        }
                    }
                )
            }
    }
    //endregion

    //region function
    fun setSection(item: Section?) {
        if (item == null) return
        tWarehouse.setItem<Warehouse>(item.idWarehouse) {
            onEventPauseCallBackItemSelect = {
                item
            }
        }
    }
    fun setSection(item: Long) {
        if (item <= 0) return
        val section = SectionViewModel(app).viewModelView(item) ?: return
        setSection(section)
    }
    //endregion
}