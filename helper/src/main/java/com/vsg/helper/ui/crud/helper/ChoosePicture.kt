package com.vsg.helper.ui.crud.helper

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import com.vsg.helper.R
import com.vsg.helper.helper.HelperUI
import com.vsg.helper.helper.HelperUI.Static.getBitmap
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutLinealLayout
import com.vsg.helper.helper.HelperUI.Static.makeCustomLayoutRelativeLayout
import com.vsg.helper.helper.HelperUI.Static.setPictureFromFile
import com.vsg.helper.helper.array.HelperArray.Companion.toBitmap
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toArray
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.toRotate
import com.vsg.helper.helper.file.HelperFile
import com.vsg.helper.helper.file.HelperFile.Static.chooserFile
import com.vsg.helper.helper.file.HelperFile.Static.getTempFileFromUri
import com.vsg.helper.helper.file.TypeTempFile
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.toPixel
import com.vsg.helper.ui.popup.viewer.picture.UICustomDialogViewerEditor
import com.vsg.helper.ui.popup.viewer.picture.UICustomDialogViewerParameter
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.helper.ui.widget.imageView.CustomImageViewDobleTap
import java.io.File


class ChoosePicture(
    view: View,
    val activity: BaseActivity,
    val format: TypeFormatChoosePicture = TypeFormatChoosePicture.DEFAULT
) {

    private lateinit var tContainer: RelativeLayout
    lateinit var tPicture: CustomImageViewDobleTap
    private lateinit var tChoosePhoto: ImageView
    private lateinit var tChooseImage: ImageView
    private lateinit var tChooseRotate: ImageView
    private lateinit var tChooseDelete: ImageView

    //    private var mapRotate: MutableList<Pair<Int, Float>> = mutableListOf()
//    private var angle: Int = 1
    private var fileToPhoto: File? = null
    private var currentUri: Uri? = null
//    var outDirectory: String = ""


//    private lateinit var mPhotoEditor: PhotoEditor

    var onEventGetPicture: ((Bitmap?, ByteArray?, Uri?) -> Unit)? = null

    enum class TypeFormatChoosePicture {
        DEFAULT,
        COMMAND_BOTTOM
    }


    init {
//        mapRotate = activity.makeMapRotate()
        tContainer = view.findViewById(R.id.DialogGenericPictureChooseContainer)

        // set format of view:
        setChooseFormat()

        this.tPicture.apply {
            onEventDoubleTap = { _, b ->
                if (b != null) {
                    UICustomDialogViewerEditor(activity).apply {
                        this.onEventGetPicture = { picture ->
                            if (picture != null) setPictureFromBitmap(picture)
                        }
                        make(UICustomDialogViewerParameter(b))
                    }
                }
            }
        }
        this.tChooseImage.apply {
            setOnClickListener {
                activity.chooserFile(TypeTempFile.IMAGE_CHOOSER_FILE)
            }
        }
        this.tChoosePhoto.apply {
            setOnClickListener {
                activity.takeImageWithCamera(formatCamera = true)
            }
        }
        this.tChooseRotate.apply {
            setOnClickListener { rotate() }
        }
        this.tChooseDelete.apply {
            setOnClickListener { tPicture.setImageBitmap(null) }
        }

        activity.onEventExecuteActivityResult = { requestCode, resultCode, data ->
            this.currentUri = null
            if (requestCode == HelperUI.REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER && resultCode == Activity.RESULT_OK) {
                val file: File? = activity.getTempFileFromUri(
                    data?.data,
                    HelperFile.SUB_PATH_TEMP_CHOOSER_FILE,
                    TypeTempFile.IMAGE_CHOOSER_FILE
                )
                setPictureFromFile(file)
            }
            if (requestCode == HelperUI.REQUEST_FOR_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
                try {
                    if (fileToPhoto != null) setPictureFromFile(fileToPhoto)
                } finally {
                    if (fileToPhoto != null && fileToPhoto!!.exists()) fileToPhoto!!.delete()
                    fileToPhoto = null
                }

            }
        }
        activity.onEventPathForTakePhoto = { fileToPhoto = it }

    }

    //region format
    private fun setChooseFormat() {
        tContainer.removeAllViews()
        val c = RelativeLayout(activity)
        tPicture = setViewPicture(format)
        when (format) {
            TypeFormatChoosePicture.DEFAULT -> {
                tChoosePhoto = setCommand(
                    format, arrayListOf(RelativeLayout.ALIGN_TOP, RelativeLayout.START_OF),
                    drawable = R.drawable.pic_take_photo
                )
                tChooseImage = setCommand(
                    format, arrayListOf(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.END_OF),
                    drawable = R.drawable.pic_choose_image
                )
                tChooseRotate = setCommand(
                    format, arrayListOf(RelativeLayout.ALIGN_TOP, RelativeLayout.END_OF),
                    drawable = R.drawable.pic_choose_rotate
                )
                tChooseDelete = setCommand(
                    format, arrayListOf(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.START_OF),
                    drawable = R.drawable.pic_choose_delete
                )
                arrayOf(tPicture, tChoosePhoto, tChooseImage, tChooseRotate, tChooseDelete)
                    .forEach {
                        c.addView(it)
                    }
            }
            TypeFormatChoosePicture.COMMAND_BOTTOM -> {
                tChoosePhoto = setCommand(format, drawable = R.drawable.pic_take_photo)
                tChooseImage = setCommand(format, drawable = R.drawable.pic_choose_image)
                tChooseRotate = setCommand(format, drawable = R.drawable.pic_choose_rotate)
                tChooseDelete = setCommand(format, drawable = R.drawable.pic_choose_delete)

                val commandsContainer: LinearLayout = LinearLayout(activity).apply {
                    layoutParams = makeCustomLayoutLinealLayout().apply {
                        height = LinearLayout.LayoutParams.MATCH_PARENT
                        width = LinearLayout.LayoutParams.WRAP_CONTENT
                    }
                    orientation = LinearLayout.HORIZONTAL
                }
                arrayOf(tChoosePhoto, tChooseImage, tChooseRotate, tChooseDelete)
                    .forEach {
                        commandsContainer.addView(it)
                    }
                c.apply {
                    addView(RelativeLayout(activity).apply {
                        layoutParams = makeCustomLayoutRelativeLayout().apply {
                            height = RelativeLayout.LayoutParams.WRAP_CONTENT
                            width = RelativeLayout.LayoutParams.MATCH_PARENT
                            addRule(RelativeLayout.ALIGN_PARENT_TOP)
                        }
                        addView(commandsContainer)
                    })
                    addView(tPicture)
                }
            }
        }
        tContainer.addView(c)
    }

    private fun setCommand(
        format: TypeFormatChoosePicture,
        rules: ArrayList<Int>? = null,
        dimension: Int = 48,
        @DrawableRes drawable: Int = 0
    ): ImageView {
        return ImageView(activity).apply {
            val dimensionCommand = dimension.toPixel(activity)
            layoutParams = when (format) {
                TypeFormatChoosePicture.DEFAULT -> {
                    makeCustomLayoutRelativeLayout().apply {
                        height = dimensionCommand
                        width = dimensionCommand
                        if (rules != null && rules.any()) {
                            rules.forEach {
                                addRule(it, tPicture.id)
                            }
                        }
                    }
                }
                TypeFormatChoosePicture.COMMAND_BOTTOM -> {
                    makeCustomLayoutLinealLayout().apply {
                        height = dimensionCommand
                        width = 0
                        weight = 1F
                    }
                }
            }
            setImageBitmap(BitmapFactory.decodeResource(resources, drawable))
        }
    }

    private fun setViewPicture(format: TypeFormatChoosePicture): CustomImageViewDobleTap {
        val dimensionCommand = 256.toPixel(activity)
        return CustomImageViewDobleTap(activity).apply {
            id = View.generateViewId()
            layoutParams = when (format) {
                TypeFormatChoosePicture.DEFAULT -> {
                    makeCustomLayoutRelativeLayout().apply {
                        height = dimensionCommand
                        width = dimensionCommand
                        addRule(RelativeLayout.CENTER_IN_PARENT)
                    }
                }
                TypeFormatChoosePicture.COMMAND_BOTTOM -> {
                    makeCustomLayoutRelativeLayout().apply {
//                        height = RelativeLayout.LayoutParams.MATCH_PARENT
                        width = RelativeLayout.LayoutParams.MATCH_PARENT
                        height = dimensionCommand
//                        width = dimensionCommand
                        addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        topMargin = 65.toPixel(activity)
                    }
                }
            }
//            this.setBackgroundColor(Color.RED)
        }
    }
    //endregion

    private fun setPictureFromFile(file: File?) {
        if (file != null) {
            tPicture.setPictureFromFile(file)
//            rotate()
            file.delete()
            this.currentUri = Uri.fromFile(file)
            onEventGetPicture?.invoke(getBitmap(), getArray(), currentUri)
        }
    }

    private fun setPictureFromBitmap(bitmap: Bitmap?) {
        if (bitmap != null) {
            tPicture.setImageBitmap(bitmap)
        }
    }

    private fun rotate() {
        val data: Bitmap? = tPicture.getBitmap()
//        tPicture.setImageBitmap(null)
//        val rotation = angle.nextRotation(mapRotate)
//        this.angle = rotation.first
        tPicture.setImageBitmap(data.toRotate(90F))
    }

    private fun getBitmap(): Bitmap? = tPicture.getBitmap()
    fun getArray(): ByteArray? = getBitmap().toArray()

    fun setBitmap(bitmap: Bitmap?) {
        tPicture.setImageBitmap(bitmap)
    }

    fun setBitmap(bitmap: ByteArray?) {
        tPicture.setImageBitmap(bitmap.toBitmap())
    }
}