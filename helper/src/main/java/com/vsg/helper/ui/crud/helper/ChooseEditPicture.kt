package com.vsg.helper.ui.crud.helper

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.vsg.helper.R


class ChooseEditPicture() : AppCompatActivity() {
    val PICK_IMAGE_CODE = 10000
    val DS_PHOTO_EDITOR_REQUEST_CODE = 20000

    private val REQUEST_EXTERNAL_STORAGE_CODE = 1000000

    val OUTPUT_PHOTO_DIRECTORY = "ds_photo_editor_sample"

    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_edit_picture)
        imageView = findViewById(R.id.mainImageView)
    }

//    fun onClick(view: View) {
//        when (view.getId()) {
//            R.id.openGalleryButton -> verifyStoragePermissionsAndPerformOperation(
//                REQUEST_EXTERNAL_STORAGE_CODE
//            )
//        }
//    }

    fun verifyStoragePermissionsAndPerformOperation(requestPermissionCode: Int = REQUEST_EXTERNAL_STORAGE_CODE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_CODE)
        } else {
            // Check if we have storage permission
            val permission =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // Request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestPermissionCode
                )
            } else {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_CODE)
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("The app needs this permission to edit photos on your device.")
            builder.setPositiveButton("Update Permission",
                DialogInterface.OnClickListener { dialog, which ->
                    verifyStoragePermissionsAndPerformOperation(
                        REQUEST_EXTERNAL_STORAGE_CODE
                    )
                })
            builder.setCancelable(false)
            builder.create().show()
        }
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
    }

    /* Handle the results */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_CODE -> {
                    val inputImageUri: Uri? = data?.data
                    if (inputImageUri != null) {
                        val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
                        dsPhotoEditorIntent.data = inputImageUri

                        // This is optional. By providing an output directory, the edited photo
                        // will be saved in the specified folder on your device's external storage;
                        // If this is omitted, the edited photo will be saved to a folder
                        // named "DS_Photo_Editor" by default.
                        dsPhotoEditorIntent.putExtra(
                            DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                            OUTPUT_PHOTO_DIRECTORY
                        )

                        // You can also hide some tools you don't need as below
//                        int[] toolsToHide = {DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_ORIENTATION};
//                        dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                        startActivityForResult(dsPhotoEditorIntent, DS_PHOTO_EDITOR_REQUEST_CODE)
                    } else {
                        Toast.makeText(
                            this,
                            "Please select an image from the Gallery",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                DS_PHOTO_EDITOR_REQUEST_CODE -> {
                    val outputUri: Uri? = data?.data
                    imageView?.setImageURI(outputUri)
                    Toast.makeText(
                        this,
                        "Photo saved in $OUTPUT_PHOTO_DIRECTORY folder.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}