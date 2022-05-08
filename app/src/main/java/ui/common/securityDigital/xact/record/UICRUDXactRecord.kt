package com.vsg.ot.ui.common.securityDigital.xact.record

import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.date.HelperDate.Companion.date
import com.vsg.helper.helper.date.HelperDate.Companion.nowDate
import com.vsg.helper.helper.exception.HelperException.Companion.throwException
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.crud.helper.ChoosePicture
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.spinner.CustomSpinner
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventViewModel
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordDao
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordViewModel
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorViewModel
import common.model.master.item.type.TypePlant

@ExperimentalStdlibApi
class UICRUDXactRecord<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, XactRecordViewModel, XactRecordDao, XactRecord>(
        activity,
        operation,
        R.layout.dialog_security_dialog_xact_record
    )
        where TActivity : CurrentBaseActivity<XactRecordViewModel, XactRecordDao, XactRecord> {

    //region widget
    private lateinit var tCaption: CustomInputText
    private lateinit var tEvent: CustomSpinner
    private lateinit var tDate: CustomInputText
    private lateinit var tSector: CustomSpinner
    private lateinit var tPlant: CustomSpinner

    private val formatDate = FormatDateString.CREATE_DATE
    private lateinit var event: XactEvent
    private lateinit var sector: XactSector
    private lateinit var choosePicture: ChoosePicture
    //endregion

    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<XactRecord>()

    init {
        onEventSetInit = {
            choosePicture = ChoosePicture(
                it,
                activity,
                ChoosePicture.TypeFormatChoosePicture.COMMAND_BOTTOM
            ).apply {

            }
            this.tCaption = it.findViewById(R.id.DialogXactRecordTitle)

            this.tPlant = it.findViewById<CustomSpinner>(R.id.DialogXactRecordPlant).apply {
                setCustomAdapterEnum(TypePlant::class.java)
            }
            this.tSector = it.findViewById<CustomSpinner>(R.id.DialogXactRecordSector).apply {
                setCustomAdapter(
                    data = makeViewModel(XactSectorViewModel::class.java).viewModelViewAllSimpleList(),
                    callBackItemSelect = { sec ->
                        if (sec != null) this@UICRUDXactRecord.sector = sec
                    },
                )
            }
            this.tEvent = it.findViewById<CustomSpinner>(R.id.DialogXactRecordEvent).apply {
                setCustomAdapter(
                    data = makeViewModel(XactEventViewModel::class.java).viewModelViewAllSimpleList(),
                    callBackItemSelect = { sec ->
                        if (sec != null) this@UICRUDXactRecord.event = sec
                    },
                )
            }

            this.tDate = it.findViewById<CustomInputText>(R.id.DialogXactRecordDate).apply {
                customFormatDate = this@UICRUDXactRecord.formatDate
                setDatePicker(activity) { nowDate() }
                date = nowDate()
            }
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: XactRecord()
            data.apply {
                this.caption = tCaption.text
                this.idEvent = this@UICRUDXactRecord.event.id ?: 0
                this.event = this@UICRUDXactRecord.event
                this.idSector = this@UICRUDXactRecord.sector.id ?: 0
                this.sector = this@UICRUDXactRecord.sector
                this.planta = tPlant.getItemEnumOrDefault() ?: TypePlant.UNDEFINED
                this.createDate = tDate.date ?: nowDate()
                this.picture = choosePicture.getArray()
            }
            data
        }
        onEventSetItem = {
            tCaption.text = it.caption
            tPlant.setItemEnum(it.planta)
            tSector.setItem<XactSector>(it.idSector)
            tEvent.setItem<XactEvent>(it.idEvent)
            tDate.date = it.createDate
            choosePicture.setBitmap(it.picture)
        }
        onEventSetItemsForClean = {
            mutableListOf(tCaption, choosePicture.tPicture, tDate)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.caption.isEmpty()) "El nombre del Evento no puede ser nulo".throwException()
                if (item.createDate <= nowDate().date()) "La fecha no puede ser inferior a la fecha actual".throwException()
                if (item.idEvent <= 0) "No se ha seleccionado un Evento".throwException()
                if (item.idSector <= 0) "No se ha seleccionado un Sector".throwException()
                if (item.planta == TypePlant.UNDEFINED) "No se ha seleccionado una Planta".throwException()
                if (item.picture == null || item.picture!!.isEmpty()) "La imagen no fue asignado".throwException()
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.45
            if (item != null) {
                val temp = getEntityWithRelation(XactRecordViewModel::class.java, item.id) ?: item
                p?.icon = temp.getDrawableShow().drawable
                p?.bitmap = temp.getPictureShow()
                p?.toHtml = temp.reference()
            }
            p
        }
    }
}