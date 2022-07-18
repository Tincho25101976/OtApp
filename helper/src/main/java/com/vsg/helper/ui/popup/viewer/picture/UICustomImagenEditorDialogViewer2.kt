package com.vsg.helper.ui.popup.viewer.picture

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.getBitmap
import com.vsg.helper.helper.HelperUI.Static.toEditable
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toRotate
import com.vsg.helper.helper.file.HelperFile.Static.getTempFileStore
import com.vsg.helper.helper.file.TypeTempFile
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.helper.screenshot.HelperScreenShot
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.ui.popup.UICustomAlertDialogBase
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.colorPicker.ColorPicker
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.SaveSettings
import java.io.File


class UICustomImagenEditorDialogViewer2<TActivity>(activity: TActivity) :
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
    //endregion

    //region properties
    private var type: TypeOperation = TypeOperation.ROTATE
    private lateinit var bitmap: Bitmap
    private var initialLoad: Boolean = true


    //region views:
    private lateinit var tEditPhotoView: PhotoEditorView
    private lateinit var mPhotoEditor: PhotoEditor
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
    private lateinit var tColorPickerLine: ColorPicker
    private lateinit var tSeekSizeLine: SeekBar
    private lateinit var tSampleLine: TextView
    private var tBrushDraw: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }
    //endregion

    //region rotate
    private var positionRotate: Float = 0F
    private val dbRotate: List<Pair<Int, Float>>
        get() =
            listOf(
                0 to 0F,
                1 to 90F,
                2 to 180F,
                3 to 270F
            )
    //endregion

    //endregion

    //region methods
    init {
        onSetDialogView = { dialogView, value, _ ->
            tEditPhotoView = dialogView.findViewById(R.id.mainImageViewEdit)
            this.bitmap = value.bitmap
            makeEdit(bitmap)

            tContainer = dialogView.findViewById(R.id.CustomViewerContainerEdit)
            tContainerOptions = dialogView.findViewById(R.id.CustomViewerCommandOptionsEdit)

            tDraw =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandDrawEdit).apply {
                    setOnClickListener {
//                    setOperation(TypeOperation.DRAW)
                        makeDraw()
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
//                    setOperation(TypeOperation.CROP)
                    }
                }

            tRotate =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandRotateEdit).apply {
                    setOnClickListener {
//                        setOperation(TypeOperation.ROTATE)
//                        mPhotoEditor.setFilterEffect(PhotoFilter.FLIP_HORIZONTAL)
                        rotate()
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
//                        val temp: Bitmap = getBitmap() ?: return@setOnClickListener
//                        val file: File =
//                            temp.getTempFileStoreViewer(activity) ?: return@setOnClickListener
//                        activity.sendFile(file)
                    }
                }

            activity.onEventExecuteActivityResult = { request, result, data ->
                if (data != null || data?.data != null) {
                    if (request == HelperUI.REQUEST_FOR_SEND_PICTURE && result == Activity.RESULT_OK) {
                        addPicture(data.data!!)
                    }
                }
            }
        }

    }

    //region operation
//    @SuppressLint("ClickableViewAccessibility")
//    private fun setOperation(type: TypeOperation) {
//        if (!initialLoad) if (this.type == type) return
//        tContainer.removeAllViews()
//        var heightResult = MARGIN_BOTTOM_NORMAL
//        this.typeLast = this.type
//        when (type) {
//            TypeOperation.ROTATE -> {
//                tViewImage = ImageView(activity).apply {
//                    layoutParams = setParameterLayout()
//                }
//                tViewImage?.setImageBitmap(this.getBitmap())
//                tContainer.addView(tViewImage)
//            }
//            TypeOperation.DRAW -> {
//                makeDraw()
//                heightResult = MARGIN_BOTTOM_DRAW
//                tViewDraw = DrawLineImage(getContext()!!).apply {
//                    layoutParams = setParameterLayout()
//                }
//                tViewDraw?.setBrushToDraw(tBrushDraw)
//                tViewDraw?.setImageBitmap(this.getBitmap())
//                tContainer.addView(tViewDraw)
//            }
//            TypeOperation.ZOOM -> {
//                if (tViewZoom == null) {
//                    tViewZoom = ImageView(getContext()!!).apply {
//                        layoutParams = setParameterLayout()
//                    }
////                    tViewZoom?.setOnTouchListener(ImageMatrixTouchHandler(activity))
//                }
//                tViewZoom?.imageMatrix = Matrix()
//                tViewZoom?.scaleType = ImageView.ScaleType.CENTER_CROP
//                tViewZoom?.setImageBitmap(this.getBitmap())
//                tContainer.addView(tViewZoom)
//            }
//            TypeOperation.CROP -> {
//                tViewCrop =
//                    CropImageView(getContext()).apply { layoutParams = setParameterLayout() }
//                tViewCrop?.imageBitmap = this.getBitmap()
//                tContainer.addView(tViewCrop)
//            }
//            else -> Unit
//        }
//        TransitionManager.beginDelayedTransition(tContainer, AutoTransition())
//        tContainer.layoutParams = tContainer.getCustomLayoutRelativeLayout().apply {
//            height = RelativeLayout.LayoutParams.MATCH_PARENT
//            width = RelativeLayout.LayoutParams.MATCH_PARENT
//            addRule(RelativeLayout.ALIGN_PARENT_TOP)
//            bottomMargin = heightResult
//        }
//        initialLoad = false
//        this.type = type
//    }
    //endregion

    //region draw
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeDraw() {
        tContainerOptions.removeAllViews()
        if (this.getContext() == null || this.dialogView == null) return
        tColorPickerLine = ColorPicker(getContext()!!).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply { height = 65.toPixel() }
            onEventChangeColorPicker = {
                tBrushDraw.color = it
                tSampleLine.setBackgroundColor(it)
//                if (tViewDraw != null) tViewDraw?.setBrushToDraw(tBrushDraw)
            }
        }
        tSeekSizeLine = SeekBar(getContext()).apply {
            max = MAXIMO_SIZE_DRAW * FACTOR_SIZE_DRAW
            min = MINIMO_SIZE_DRAW * FACTOR_SIZE_DRAW
            progress = (DEFAULT_SIZE_DRAW * FACTOR_SIZE_DRAW).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    tBrushDraw.strokeWidth = when (seekBar != null) {
                        true -> seekBar.progress.toFloat() / FACTOR_SIZE_DRAW
                        false -> DEFAULT_SIZE_DRAW
                    }
                    tSampleLine.text =
                        tBrushDraw.strokeWidth.toFormat(DECIMAL_SHOW_SIZE_DRAW).toEditable()
//                    if (tViewDraw != null) tViewDraw?.setBrushToDraw(tBrushDraw)

                }
            })
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        tSampleLine = TextView(getContext()).apply {
            text = null
            setBackgroundColor(Color.WHITE)
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                height = 32.toPixel()
                width = 32.toPixel()
            }
            background = context.getDrawable(R.drawable.border_view)
            gravity = Gravity.CENTER
            textSize = 7.toPixel().toFloat()
            text = tBrushDraw.strokeWidth.toFormat(DECIMAL_SHOW_SIZE_DRAW).toEditable()
            setBackgroundColor(tColorPickerLine.color)
            typeface = activity.typeFaceCustom(Typeface.BOLD_ITALIC)
        }
        val add = LinearLayout(getContext()).apply {
            layoutParams = HelperUI.makeCustomLayoutRelativeLayout().apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = 36.toPixel()
                gravity = Gravity.CENTER
            }
            orientation = LinearLayout.HORIZONTAL
            addView(tSampleLine)
            addView(tSeekSizeLine)
        }
        tContainerOptions.apply {
            addView(tColorPickerLine)
            addView(add)
        }
    }
    //endregion

    //region rotate
    private fun rotate() {
        if (!this::tEditPhotoView.isLateinit) return
//        if (positionRotate >= 360F) positionRotate = 0F
//        positionRotate += 90F
        val bitmap = this.tEditPhotoView.source.getBitmap() ?: return
        this.tEditPhotoView.source.setImageBitmap(bitmap.toRotate(90F))
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

        val saveSettings: SaveSettings = SaveSettings.Builder()
            .setClearViewsEnabled(true)
            .setTransparencyEnabled(true)
            .setCompressFormat(Bitmap.CompressFormat.PNG)
            .setCompressQuality(90)
            .build()
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

    private fun getArray(): ByteArray? {
        if (getBitmap() == null) return null
        return getBitmap().toArray()
    }

    //region layout
    private fun View.setParameterLayout(): RelativeLayout.LayoutParams =
        HelperUI.makeCustomLayoutRelativeLayout().apply {
            height = RelativeLayout.LayoutParams.WRAP_CONTENT
            width = RelativeLayout.LayoutParams.WRAP_CONTENT
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }
    //endregion

    //endregion

    companion object {
        const val MINIMO_SIZE_DRAW: Int = 10
        const val MAXIMO_SIZE_DRAW: Int = 35
        const val FACTOR_SIZE_DRAW: Int = 10
        const val DEFAULT_SIZE_DRAW: Float = 12F
        const val DECIMAL_SHOW_SIZE_DRAW: Int = 1

        const val MARGIN_BOTTOM_NORMAL = 100
        const val MARGIN_BOTTOM_DRAW = 375
    }
}