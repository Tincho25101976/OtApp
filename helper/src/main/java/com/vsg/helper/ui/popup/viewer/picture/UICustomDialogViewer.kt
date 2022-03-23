package com.vsg.helper.ui.popup.viewer.picture

import android.annotation.SuppressLint
import android.graphics.*
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI.Static.getBitmap
import com.vsg.helper.helper.HelperUI.Static.getCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.toEditable
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.makeMapRotate
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.nextRotation
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toRotate
import com.vsg.helper.helper.file.HelperFile.Static.sendFile
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.getTempFileStoreViewer
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.ui.popup.UICustomAlertDialogBase
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.colorPicker.ColorPicker
import com.vsg.helper.ui.widget.cropImage.CropImageView
import com.vsg.helper.ui.widget.imageView.DrawLineImage
import com.vsg.helper.util.helper.HelperNumeric.Companion.toFormat
import java.io.File


class UICustomDialogViewer<TActivity>(activity: TActivity) :
    UICustomAlertDialogBase<TActivity, UICustomDialogViewerParameter>(
        activity,
        R.layout.custom_viewer
    ) where TActivity : BaseActivity {

    enum class TypeOperation {
        ROTATE,
        DRAW,
        ZOOM,
        CROP,
        UNDEFINED;

        override fun toString(): String = name
    }

    //region customView
    private var tViewImage: ImageView? = null
    private var tViewDraw: DrawLineImage? = null //DrawLineCanvas? = null
    private var tViewZoom: ImageView? = null
    private var tViewCrop: CropImageView? = null
    //endregion

    //region views
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
    private var mapRotate: MutableList<Pair<Int, Float>> = mutableListOf()
    private var angle: Int = 1
    //endregion

    //region process
    private lateinit var bitmap: Bitmap
    private lateinit var bitmapProcess: Bitmap
    private var initialLoad: Boolean = true
    private var type: TypeOperation = TypeOperation.ROTATE
    private var typeLast: TypeOperation = TypeOperation.UNDEFINED
    var onEventGetPicture: ((Bitmap?, ByteArray?) -> Unit)? = null
    //endregion

    init {
        mapRotate = activity.makeMapRotate()

        onSetDialogView = { dialogView, data, _ ->
            bitmap = data.bitmap.copy(Bitmap.Config.ARGB_8888, true)
            bitmapProcess = data.bitmap.copy(Bitmap.Config.ARGB_8888, true)
            tContainer = dialogView.findViewById(R.id.CustomViewerContainer)
            tContainerOptions = dialogView.findViewById(R.id.CustomViewerCommandOptions)

            tDraw = dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandDraw).apply {
                setOnClickListener {
                    setOperation(TypeOperation.DRAW)
                }
            }
            tZoom = dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandZoom).apply {
                setOnClickListener { setOperation(TypeOperation.ZOOM) }
            }
            tCrop = dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandCrop).apply {
                setOnClickListener { setOperation(TypeOperation.CROP) }
            }

            tRotate =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandRotate).apply {
                    setOnClickListener {
                        setOperation(TypeOperation.ROTATE)
                        rotate()
                    }
                }
            tOK = dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandOK).apply {
                setOnClickListener {
                    onEventGetPicture?.invoke(getBitmap(), getArray())
                    dismiss()
                }
            }

            tExport =
                dialogView.findViewById<RelativeLayout>(R.id.CustomViewerCommandSend).apply {
                    setOnClickListener {
                        val temp: Bitmap = getBitmap() ?: return@setOnClickListener
                        val file: File =
                            temp.getTempFileStoreViewer(activity) ?: return@setOnClickListener
                        activity.sendFile(file)
                    }
                }

            setOperation(TypeOperation.ROTATE)
        }
    }

    //region operation
    @SuppressLint("ClickableViewAccessibility")
    private fun setOperation(type: TypeOperation) {
        if (!initialLoad) if (this.type == type) return
        tContainer.removeAllViews()
        var heightResult = MARGIN_BOTTOM_NORMAL
        this.typeLast = this.type
        when (type) {
            TypeOperation.ROTATE -> {
                tViewImage = ImageView(activity).apply {
                    layoutParams = setParameterLayout()
                }
                tViewImage?.setImageBitmap(this.getBitmap())
                tContainer.addView(tViewImage)
            }
            TypeOperation.DRAW -> {
                makeDraw()
                heightResult = MARGIN_BOTTOM_DRAW
                tViewDraw = DrawLineImage(getContext()!!).apply {
                    layoutParams = setParameterLayout()
                }
                tViewDraw?.setBrushToDraw(tBrushDraw)
                tViewDraw?.setImageBitmap(this.getBitmap())
                tContainer.addView(tViewDraw)
            }
            TypeOperation.ZOOM -> {
                if (tViewZoom == null) {
                    tViewZoom = ImageView(getContext()!!).apply {
                        layoutParams = setParameterLayout()
                    }
                    tViewZoom?.setOnTouchListener(ImageMatrixTouchHandler(activity))
                }
                tViewZoom?.imageMatrix = Matrix()
                tViewZoom?.scaleType = ImageView.ScaleType.CENTER_CROP
                tViewZoom?.setImageBitmap(this.getBitmap())
                tContainer.addView(tViewZoom)
            }
            TypeOperation.CROP -> {
                tViewCrop =
                    CropImageView(getContext()).apply { layoutParams = setParameterLayout() }
                tViewCrop?.imageBitmap = this.getBitmap()
                tContainer.addView(tViewCrop)
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
        initialLoad = false
        this.type = type
    }
    //endregion

    //region rotate
    private fun rotate() {
        tViewImage?.setImageBitmap(null)
        val rotation = angle.nextRotation(mapRotate)
        this.angle = rotation.first
        tViewImage?.setImageBitmap(this.bitmapProcess.toRotate(rotation.second))
    }
    //endregion

    //region draw
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeDraw() {
        tContainerOptions.removeAllViews()
        if (this.getContext() == null || this.dialogView == null) return
        tColorPickerLine = ColorPicker(getContext()!!).apply {
            layoutParams = makeCustomLayoutRelativeLayout().apply { height = 65.toPixel() }
            onEventChangeColorPicker = {
                tBrushDraw.color = it
                tSampleLine.setBackgroundColor(it)
                if (tViewDraw != null) tViewDraw?.setBrushToDraw(tBrushDraw)
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
                    if (tViewDraw != null) tViewDraw?.setBrushToDraw(tBrushDraw)

                }
            })
            layoutParams = makeCustomLayoutRelativeLayout().apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
            }
        }
        tSampleLine = TextView(getContext()).apply {
            text = null
            setBackgroundColor(Color.WHITE)
            layoutParams = makeCustomLayoutRelativeLayout().apply {
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
            layoutParams = makeCustomLayoutRelativeLayout().apply {
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

    //region result
    fun getBitmap(): Bitmap? {
        bitmapProcess = try {
            when (typeLast) {
                TypeOperation.ROTATE -> tViewImage?.getBitmap() ?: bitmap
                TypeOperation.DRAW -> tViewDraw?.getBitmap() ?: bitmap
                TypeOperation.ZOOM -> tViewZoom?.getBitmap() ?: bitmap
                TypeOperation.CROP -> tViewCrop?.getBitmap() ?: bitmap
                TypeOperation.UNDEFINED -> bitmap
            }
        } catch (e: Exception) {
            bitmap
        }
        return bitmapProcess.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun getArray(): ByteArray? = getBitmap().toArray()
    //endregion

    //region layout
    private fun View.setParameterLayout(): RelativeLayout.LayoutParams =
        makeCustomLayoutRelativeLayout().apply {
            height = RelativeLayout.LayoutParams.WRAP_CONTENT
            width = RelativeLayout.LayoutParams.WRAP_CONTENT
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }
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