package ui.common.xact

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
import common.model.securityDialog.xact.Xact
import common.model.securityDialog.xact.XactDao
import common.model.securityDialog.xact.XactViewModel
import java.io.File

@ExperimentalStdlibApi
class UICRUXact<TActivity>(activity: TActivity, operation: DBOperation) :
    UICustomCRUDViewModel<TActivity, XactViewModel, XactDao, Xact>(
        activity,
        operation,
        R.layout.dialog_category
    )
        where TActivity : CurrentBaseActivity<XactViewModel, XactDao, Xact> {

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
            val data = it ?: Xact()
            data.apply {
                this.description = tName.text
                this.picture = tPicture.getArray()
            }
            data
        }
        onEventSetItem = {
            tName.text = it.caption
            tPicture.setImageBitmap(it.picture.toBitmap())
        }
        onEventSetItemsForClean = {
            mutableListOf(tName, tPicture)
        }
        onEventValidate = { item, _ ->
            var result = false
            try {
                if (item.caption.isEmpty()) throw Exception("El nombre de la Empresa no puede ser nulo...")
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
                p?.icon = item.getDrawableShow().drawable
                p?.bitmap = item.getPictureShow()
            }
            p
        }
    }
}