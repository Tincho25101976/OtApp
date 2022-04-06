package com.vsg.ot.ui.activities.master.helper

import android.app.Application
import android.view.View
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.section.MasterSection
import common.model.master.section.MasterSectionViewModel
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.MasterWarehouseViewModel

@ExperimentalStdlibApi
class ChooseSectionByWarehouse(
    view: View,
    private val app: Application,
    private val company: MasterCompany
) {
    //region handler
    var onEventSectionSelected: ((MasterSection?) -> Unit)? = null
    private var onEventPauseCallBackItemSelect: (() -> MasterSection?)? = null
    //endregion

    //region widget
    private var tWarehouse: CustomSpinner
    private var tSection: CustomSpinner = view.findViewById(R.id.SelectSectionByWarehouseSection)
    //endregion

    //region properties
    private var warehouse: MasterWarehouse? = null
    private var section: MasterSection? = null
    //endregion

    //region init
    init {
        tWarehouse =
            view.findViewById<CustomSpinner>(R.id.SelectSectionByWarehouseWarehouse).apply {
                setCustomAdapter(
                    MasterWarehouseViewModel(app).viewModelViewAllSimpleList(company.id),
                    callBackItemSelect = {
                        this@ChooseSectionByWarehouse.warehouse = it
                        if (it != null) {
                            val setItem = onEventPauseCallBackItemSelect?.invoke()
                            tSection.apply {
                                setCustomAdapter(
                                    data = MasterSectionViewModel(app).viewModelViewAllSimpleList(it.id),
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
    fun setSection(item: MasterSection?) {
        if (item == null) return
        tWarehouse.setItem<MasterWarehouse>(item.idWarehouse) {
            onEventPauseCallBackItemSelect = {
                item
            }
        }
    }

    fun setSection(item: Int) {
        if (item <= 0) return
        val section = MasterSectionViewModel(app).viewModelView(item) ?: return
        setSection(section)
    }
    //endregion
}