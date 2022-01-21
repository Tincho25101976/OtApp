package com.vsg.helper.helper.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.vsg.helper.helper.HelperUI


class HelperPerminission {
    companion object Static{
        fun Activity.checkedPermissionStorage(): Boolean {
            if (this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            ActivityCompat.requestPermissions(this,
                                              arrayOf("android.permission.READ_EXTERNAL_STORAGE",
                                                      "android.permission.WRITE_EXTERNAL_STORAGE"),
                                              HelperUI.REQUEST_FOR_PERMISSION_CAMERA)
            return false
        }

        fun Activity.checkedPermissionCamera(): Boolean {
            if (this.checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            ActivityCompat.requestPermissions(this,
                                              arrayOf("android.permission.CAMERA"),
                                              HelperUI.REQUEST_FOR_PERMISSION_CAMERA)
            return false
        }
    }
}