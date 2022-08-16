package com.vsg.ot.ui.common.securityDigital.xact.rating

import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRating
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRatingDao
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRatingViewModel

@ExperimentalStdlibApi
class UICRUDXactRating<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, XactRatingViewModel, XactRatingDao, XactRating>(
        activity,
        operation,
        R.layout.dialog_xact_rating
    )
        where TActivity : CurrentBaseActivity<XactRatingViewModel, XactRatingDao, XactRating> {

    //region widget
    private lateinit var tValueCode: CustomInputText
    //endregion

    override fun aGetEntityAllowDefault(): Boolean = isEntityAllowDefault<XactRating>()

    init {
        onEventSetInit = {
            this.tValueCode = it.findViewById(R.id.DialogXactRatingValueCode)
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: XactRating()
            data.apply {
                this.valueCode = tValueCode.text
            }
            data
        }
        onEventSetItem = {
            tValueCode.text = it.valueCode
        }
        onEventSetItemsForClean = {
            mutableListOf(tValueCode)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.valueCode.isEmpty()) throw Exception("El nombre de la clasificación no puede ser nulo...")
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.25
            if (item != null) {
                p?.icon = item.getDrawableShow().drawable
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }
}