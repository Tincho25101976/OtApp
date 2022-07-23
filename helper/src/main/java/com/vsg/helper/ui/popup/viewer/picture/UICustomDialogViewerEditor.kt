package com.vsg.helper.ui.popup.viewer.picture

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.getCustomLayoutRelativeLayout
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toRotate
import com.vsg.helper.helper.file.HelperFile.Static.getTempFileStore
import com.vsg.helper.helper.file.HelperFile.Static.getURI
import com.vsg.helper.helper.file.HelperFile.Static.sendFile
import com.vsg.helper.helper.file.TypeTempFile
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.helper.screenshot.HelperScreenShot
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.getTempFileStoreViewer
import com.vsg.helper.ui.popup.UICustomAlertDialogBase
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.cropImage.CropImageView
import com.vsg.helper.ui.widget.shapeCustom.ShapeCustomSelect
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.SaveSettings
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import java.io.File


class UICustomDialogViewerEditor<TActivity>(activity: TActivity) :
    UICustomAlertDialogBase<TActivity,
            UICustomDialogViewerParameter>(
        activity,
        R.layout.custom_edit_image
    ) where TActivity : BaseActivity {

    enum class TypeOperation {
        ROTATE,
        DRAW,
        ZOOM,
        CROP,
        UNDEFINED;

        override fun toString(): String = name
    }

    //region event
    var onEventGetPicture: ((Bitmap?) -> Unit)? = null
    var onEventGetPictureMemory: ((Bitmap?) -> Unit)? = null
    var onEventGetPictureUri: ((Uri?) -> Unit)? = null
    //endregion

    //region properties
    private var type: TypeOperation = TypeOperation.ROTATE
    private lateinit var bitmap: Bitmap
    private var initialLoad: Boolean = true


    //region views:
    private lateinit var tEditPhotoView: PhotoEditorView
    private lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoCrop: CropImageView

    private lateinit var tRotate: RelativeLayout
    private lateinit var tExport: RelativeLayout
    private lateinit var tDraw: RelativeLayout
    private lateinit var tZoom: RelativeLayout
    private lateinit var tCrop: RelativeLayout
    private lateinit var tOK: RelativeLayout
    private lateinit var tContainer: RelativeLayout
    private lateinit var tContainerOptions: LinearLayout
    //endregion

    //region draw
    private lateinit var tShapeCustomSelect: ShapeCustomSelect
    private var tBrushDraw: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }
    private val saveSettings: SaveSettings = SaveSettings.Builder()
        .setClearViewsEnabled(true)
        .setTransparencyEnabled(true)
        .setCompressFormat(Bitmap.CompressFormat.PNG)
        .setCompressQuality(90)
        .build()
    //endregion

    //endregion

    //region methods
    init {
        onSetDialogView = { dialogView, value, _ ->
            tEditPhotoView = dialogView.findViewById(R.id.mainImageViewEdit)
            mPhotoCrop = CropImageView(this.activity).apply {
                id = View.generateViewId()
                setCropMode(CropImageView.CropMode.FIT_IMAGE)
                setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS)
                setGuideShowMode(CropImageView.ShowMode.SHOW_ALWAYS)
                setFrameStrokeWeightInDp(1)
                setGuideStrokeWeightInDp(1)
                layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                    setPadding(16, 16, 16, 16)
                }
            }
            this.bitmap = value.bitmap
            makeEdit(bitmap)

            tContainer = dialogView.findViewById(R.id.CustomViewerContainerEdit)
            tContainerOptions = dialogView.findViewById(R.id.CustomViewerCommandOptionsEdit)

            tDraw =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandDrawEdit).apply {
                    setOnClickListener {
                        setOperation(TypeOperation.DRAW)
                    }
                }
            tZoom =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandZoomEdit).apply {
                    setOnClickListener {
//                    setOperation(TypeOperation.ZOOM)
                    }
                }
            tCrop =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandCropEdit).apply {
                    setOnClickListener {
                        setOperation(TypeOperation.CROP)
                    }
                }

            tRotate =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandRotateEdit).apply {
                    setOnClickListener {
                        setOperation(TypeOperation.ROTATE)
                    }
                }
            tOK = dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandOKEdit).apply {
                setOnClickListener {
                    getBitmap()
                    dismiss()
                }
            }

            tExport =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandSendEdit).apply {
                    setOnClickListener {
                        val temp: Bitmap = getBitmap() ?: return@setOnClickListener
                        val file: File =
                            temp.getTempFileStoreViewer(activity) ?: return@setOnClickListener
                        activity.sendFile(file)
                    }
                }

//            activity.onEventExecuteActivityResult = { request, result, data ->
//                if (data != null || data?.data != null) {
//                    if (request == HelperUI.REQUEST_FOR_SEND_PICTURE && result == Activity.RESULT_OK) {
//                        addPicture(data.data!!)
//                    }
//                }
//            }
        }

    }

    //region operation
    @SuppressLint("ClickableViewAccessibility")
    private fun setOperation(type: TypeOperation) {
        tContainer.removeAllViews()
        var heightResult = MARGIN_BOTTOM_NORMAL
        mPhotoEditor.setBrushDrawingMode(false)
        when (type) {
            TypeOperation.ROTATE -> rotate()
            TypeOperation.DRAW -> {
                makeDraw()
                heightResult = MARGIN_BOTTOM_DRAW
            }
            TypeOperation.ZOOM -> {

            }
            TypeOperation.CROP -> {
                makeCrop()
                heightResult = MARGIN_BOTTOM_NORMAL
            }
            else -> Unit
        }
        TransitionManager.beginDelayedTransition(tContainer, AutoTransition())
        tContainer.layoutParams = tContainer.getCustomLayoutRelativeLayout().apply {
            height = RelativeLayout.LayoutParams.MATCH_PARENT
            width = RelativeLayout.LayoutParams.MATCH_PARENT
            addRule(RelativeLayout.ALIGN_PARENT_TOP)
            bottomMargin = heightResult
        }
//        initialLoad = true
        this.type = type
    }
    //endregion

    //region draw
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeDraw() {
        tContainerOptions.removeAllViews()
        if (this.getContext() == null || this.dialogView == null) return
        mPhotoEditor.setBrushDrawingMode(true)
        val shapeBuilder = ShapeBuilder()
        tShapeCustomSelect = ShapeCustomSelect(activity).apply {
            id = View.generateViewId()
            typeface = activity.typeFaceCustom(Typeface.BOLD)
            this.onEventChangeShape = { s -> shapeBuilder.withShapeType(s) }
            this.onEventChangeColor = { s -> shapeBuilder.withShapeColor(s) }
            this.onEventChangeSize = { s -> shapeBuilder.withShapeSize(s) }
        }
        mPhotoEditor.setShape(shapeBuilder)
        tContainer.addView(tEditPhotoView)
        tContainerOptions.addView(tShapeCustomSelect)
    }
    //endregion

    //region crop
    private fun makeCrop() {
        tContainerOptions.removeAllViews()
        if (this.getContext() == null || this.dialogView == null) return
        this.onEventGetPicture = {
            if (it != null) mPhotoCrop.imageBitmap = it
        }
        getBitmap()
        tContainer.addView(mPhotoCrop)
    }
    //endregion

    //region rotate
    private fun rotate() {
        if (!this::tEditPhotoView.isLateinit) return
        this.onEventGetPictureMemory = { picture ->
            if (picture != null) {
                this.tEditPhotoView.source.setImageBitmap(picture.toRotate(90F))
            }
        }
        saveBitmapMemory()
    }

    //endregion

    //endregion

    private fun makeEdit(bitmap: Bitmap) {
        mPhotoEditor = PhotoEditor.Builder(activity, tEditPhotoView)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .setDefaultTextTypeface(activity.typeface)
            .build().apply {
                setBrushDrawingMode(true)
            }

        tEditPhotoView.source.setImageBitmap(bitmap)
    }

    private fun addPicture(picture: Bitmap) {
        tEditPhotoView.source.setImageBitmap(picture)
    }

    private fun addPicture(picture: Uri) {
        tEditPhotoView.source.setImageURI(picture)
    }

    private fun getBitmap(): Bitmap? {
        if (!this::tEditPhotoView.isLateinit) return null
        var bitmap: Bitmap? = null

        val file: File =
            activity.getTempFileStore(
                HelperScreenShot.SUB_PATH_TEMP_FILE_EDIT_PICTURE,
                TypeTempFile.IMAGE_CHOOSER_FILE,
                clear = true
            )
                ?: return null

        val parameter = object : PhotoEditor.OnSaveListener {
            override fun onFailure(exception: Exception) {
                bitmap = null
            }

            override fun onSuccess(imagePath: String) {
                if (imagePath.isNotEmpty()) onEventGetPictureUri?.invoke(
                    activity.getURI(
                        File(
                            imagePath
                        )
                    )
                )
                bitmap = BitmapFactory.decodeFile(imagePath)
                onEventGetPicture?.invoke(bitmap)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mPhotoEditor.saveAsFile(
                file.path,
                saveSettings,
                parameter
            )
            bitmap = BitmapFactory.decodeFile(file.path)
        }
        return bitmap
    }

    private fun saveBitmapMemory() {
        if (!this::tEditPhotoView.isLateinit) return
        val parameter = object : OnSaveBitmap {
            override fun onBitmapReady(saveBitmap: Bitmap?) {
                onEventGetPictureMemory?.invoke(saveBitmap)
            }

            override fun onFailure(e: Exception?) {
            }
        }
        mPhotoEditor.saveAsBitmap(
            saveSettings,
            parameter
        )
    }

    private fun getArray(): ByteArray? {
        if (getBitmap() == null) return null
        return getBitmap().toArray()
    }

//    //region layout
//    private fun View.setParameterLayout(): RelativeLayout.LayoutParams =
//        HelperUI.makeCustomLayoutRelativeLayout().apply {
//            height = RelativeLayout.LayoutParams.WRAP_CONTENT
//            width = RelativeLayout.LayoutParams.WRAP_CONTENT
//            addRule(RelativeLayout.CENTER_IN_PARENT)
//        }
//    //endregion

    //endregion

    companion object {
        const val MINIMO_SIZE_DRAW: Int = 10
        const val MAXIMO_SIZE_DRAW: Int = 35
        const val FACTOR_SIZE_DRAW: Int = 10
        const val DEFAULT_SIZE_DRAW: Float = 12F
        const val DECIMAL_SHOW_SIZE_DRAW: Int = 1

        const val MARGIN_BOTTOM_NORMAL = 100
        const val MARGIN_BOTTOM_DRAW = 500// 375
    }
}