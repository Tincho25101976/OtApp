package com.vsg.helper.ui.popup.viewer.picture

import android.graphics.Bitmap
import com.vsg.helper.R
import com.vsg.helper.ui.popup.UICustomAlertDialogBase
import com.vsg.helper.ui.util.BaseActivity
import ja.burhanrashid52.photoeditor.PhotoEditorView

class UICustomImagenEditorDialogViewer<TActivity>(activity: TActivity) :
    UICustomAlertDialogBase<TActivity,
            UICustomDialogViewerParameter>(
        activity,
        R.layout.custom_edit_image
    ) where TActivity : BaseActivity {

    //region customView
    private lateinit var tViewImage: PhotoEditorView
    private lateinit var bitmap: Bitmap
    //endregion

    init {
        onSetDialogView = { _, data, _ ->
            bitmap = data.bitmap.copy(Bitmap.Config.ARGB_8888, true)
            tViewImage.source.setImageBitmap(this.bitmap)
        }
        tViewImage = activity.findViewById(R.id.mainImageView)
    }
}