package com.vsg.ot.ui.common.master.company

import android.app.Activity
import com.vsg.helper.common.operation.DBOperation
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.getArray
import com.vsg.helper.helper.HelperUI.Static.setPictureFromFile
import com.vsg.helper.helper.array.HelperArray.Companion.toBitmap
import com.vsg.helper.helper.file.HelperFile
import com.vsg.helper.helper.file.HelperFile.Static.chooserFile
import com.vsg.helper.helper.file.HelperFile.Static.getTempFileFromUri
import com.vsg.helper.helper.file.TypeTempFile
import com.vsg.helper.ui.crud.UICustomCRUDViewModel
import com.vsg.helper.ui.popup.viewer.picture.UICustomDialogViewer
import com.vsg.helper.ui.popup.viewer.picture.UICustomDialogViewerParameter
import com.vsg.helper.ui.util.CurrentBaseActivity
import com.vsg.helper.ui.widget.imageView.CustomImageViewDobleTap
import com.vsg.helper.ui.widget.text.CustomInputText
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyDao
import common.model.master.company.MasterCompanyViewModel
import java.io.File

@ExperimentalStdlibApi
class UICRUDCompany<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, MasterCompanyViewModel, MasterCompanyDao, MasterCompany>(
        activity,
        operation,
        R.layout.dialog_master_company
    )
        where TActivity : CurrentBaseActivity<MasterCompanyViewModel, MasterCompanyDao, MasterCompany> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tDescription: CustomInputText
    private lateinit var tPicture: CustomImageViewDobleTap
    //endregion

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogCompanyName)
            this.tDescription = it.findViewById(R.id.DialogGenericDescription)
            this.tPicture =
                it.findViewById<CustomImageViewDobleTap>(R.id.DialogCompanyPicture).apply {
                    setOnLongClickListener {
                        activity.chooserFile(TypeTempFile.IMAGE_CHOOSER_FILE)
                        true
                    }
                    onEventDoubleTap = { _, b ->
                        if (b != null) {
                            UICustomDialogViewer(activity).apply {
                                make(UICustomDialogViewerParameter(b))
                            }
                        }
                    }
                }
            activity.onEventExecuteActivityResult = { requestCode, resultCode, data ->
                if (requestCode == HelperUI.REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER && resultCode == Activity.RESULT_OK) {
                    val file: File? = activity.getTempFileFromUri(
                        data?.data,
                        HelperFile.SUB_PATH_TEMP_CHOOSER_FILE,
                        TypeTempFile.IMAGE_CHOOSER_FILE
                    )
                    if (file != null) {
                        tPicture.setPictureFromFile(file)
                        file.delete()
                    }
                }
            }
        }
        onEventGetNewOrUpdateEntity = {
            val data = it ?: MasterCompany()
            data.apply {
                this.valueCode = tName.text
                this.description = tDescription.text
                this.picture = tPicture.getArray()
            }
            data
        }
        onEventSetItem = {
            tName.text = it.valueCode
            tDescription.text = it.description
            tPicture.setImageBitmap(it.picture.toBitmap())
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tDescription, tPicture)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.valueCode.isEmpty()) throw Exception("El nombre de la Empresa no puede ser nulo...")
                if (item.picture == null || item.picture!!.isEmpty()) throw Exception("El logo no fue asignado...")
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.30
            if (item != null) {
                p?.icon = item.getDrawableShow().drawable
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }
}