package com.vsg.helper.helper.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.vsg.helper.helper.HelperUI


class HelperPermission {
    companion object Static {
//        fun Activity.checkedPermissionStorage(): Boolean {
//            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                return true
//            }
//            ActivityCompat.requestPermissions(
//                this,
////                arrayOf(
////                    "android.permission.READ_EXTERNAL_STORAGE",
////                    "android.permission.WRITE_EXTERNAL_STORAGE"
////                ),
//                arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ),
//                HelperUI.REQUEST_FOR_PERMISSION_STORAGE
//            )
//            return false
//        }
//
//        fun Activity.checkedPermissionCamera(): Boolean {
//            if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                return true
//            }
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA),
//                HelperUI.REQUEST_FOR_PERMISSION_CAMERA
//            )
//            return false
//        }
//
//        fun Activity.checkedPermissionPhoneStateAndNumbers(): Boolean {
//            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
//                && this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
//            ) {
//                return true
//            }
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_PHONE_NUMBERS
//                ),
//                HelperUI.REQUEST_FOR_PERMISSION_READ_STATE_PHONE
//            )
//            return false
//        }


        fun Activity.checkedPermissionStorage(): Boolean = checkedPermissionFor(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            HelperUI.REQUEST_FOR_PERMISSION_STORAGE
        )


        fun Activity.checkedPermissionCamera(): Boolean = checkedPermissionFor(
            this,
            arrayOf(Manifest.permission.CAMERA),
            HelperUI.REQUEST_FOR_PERMISSION_CAMERA
        )

        fun Activity.checkedPermissionPhoneStateAndNumbers(): Boolean = checkedPermissionFor(
            this,
            arrayOf(
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_PHONE_STATE
            ),
            HelperUI.REQUEST_FOR_PERMISSION_READ_STATE_PHONE
        )

        private fun checkedPermissionFor(
            activity: Activity,
            permissions: Array<String>,
            requestCode: Int
        ): Boolean {
            if (!permissions.any()) return false
//            if (permissions.all { activity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }) return true
//            ActivityCompat.requestPermissions(
//                activity,
//                permissions,
//                requestCode
//            )

            permissions.filter { it.isNotEmpty() }.forEach {
                val permission = ActivityCompat.checkSelfPermission(
                    activity,
                    it
                )
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission.
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(it),
                        requestCode
                    )
                } else {
                    return true
                }
            }

            return false
        }
    }
}