package com.vsg.agendaandpublication.ui.common.itemProduct.category

import android.app.Activity
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.category.Category
import com.vsg.agendaandpublication.common.model.itemProduct.category.CategoryDao
import com.vsg.agendaandpublication.common.model.itemProduct.category.CategoryViewModel
import com.vsg.utilities.common.operation.DBOperation
import com.vsg.utilities.helper.HelperUI
import com.vsg.utilities.helper.HelperUI.Static.getArray
import com.vsg.utilities.helper.HelperUI.Static.setPictureFromFile
import com.vsg.utilities.helper.array.HelperArray.Companion.toBitmap
import com.vsg.utilities.helper.file.HelperFile
import com.vsg.utilities.helper.file.HelperFile.Static.chooserFile
import com.vsg.utilities.helper.file.HelperFile.Static.getTempFileFromUri
import com.vsg.utilities.helper.file.TypeTempFile
import com.vsg.utilities.ui.crud.UICustomCRUDViewModel
import com.vsg.utilities.ui.popup.viewer.picture.UICustomDialogViewer
import com.vsg.utilities.ui.popup.viewer.picture.UICustomDialogViewerParameter
import com.vsg.utilities.ui.util.CurrentBaseActivity
import com.vsg.utilities.ui.widget.imageView.CustomImageViewDobleTap
import com.vsg.utilities.ui.widget.text.CustomInputText
import java.io.File

@ExperimentalStdlibApi
class UICRUDCategory<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, CategoryViewModel, CategoryDao, Category>(
        activity,
        operation,
        R.layout.dialog_category
    )
        where TActivity : CurrentBaseActivity<CategoryViewModel, CategoryDao, Category> {

    //region widget
    private lateinit var tName: CustomInputText
    private lateinit var tPicture: CustomImageViewDobleTap //ImageView
    //endregion

    init {
        onEventSetInit = {
            this.tName = it.findViewById(R.id.DialogCategoryName)
            this.tPicture =
                it.findViewById<CustomImageViewDobleTap>(R.id.DialogCategoryPicture).apply {
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
            val data = it ?: Category()
            data.apply {
                this.name = tName.text
                this.picture = tPicture.getArray()
            }
            data
        }
        onEventSetItem = {
            tName.text = it.name
            tPicture.setImageBitmap(it.picture.toBitmap())
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tPicture)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.name.isEmpty()) throw Exception("El nombre de la Empresa no puede ser nulo...")
                if (item.picture == null || item.picture!!.isEmpty()) throw Exception("El logo no fue asignado...")
                result = true
            } catch (e: Exception) {
                message(e.message ?: "Error desconocido...")
            }
            result
        }
        onEventGetPopUpDataParameter = { p, item ->
            p?.factorHeight = 0.25
            if (item != null) {
                p?.icon = item.getDrawableShow()
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }
}