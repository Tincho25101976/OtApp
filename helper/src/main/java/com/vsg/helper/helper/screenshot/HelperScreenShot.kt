package com.vsg.helper.helper.screenshot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.util.TypedValue
import android.view.PixelCopy
import android.view.View
import android.view.Window
import com.vsg.helper.helper.file.TypeTempFile
import java.io.File
import java.io.FileOutputStream
import kotlin.jvm.internal.Intrinsics


class HelperScreenShot {
    companion object Static {
        private const val SUB_PATH_TEMP_FILE_SCREENSHOT = "tempScreenShot"
        private const val SUB_PATH_TEMP_FILE_VIEWER = "tempImageViewer"
        internal const val SUB_PATH_TEMP_FILE_CAMERA = "tempFileCamera"


        fun Window.getScreenShotFromView(view: View, callback: (Bitmap) -> Unit) {
            var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val locationOfViewInWindow = IntArray(2)
            view.getLocationInWindow(locationOfViewInWindow)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PixelCopy.request(this,
                                      Rect(locationOfViewInWindow[0],
                                           locationOfViewInWindow[1],
                                           locationOfViewInWindow[0] + view.width,
                                           locationOfViewInWindow[1] + view.height),
                                      bitmap,
                                      { copyResult ->
                                          if (copyResult == PixelCopy.SUCCESS) {
                                              callback(bitmap)
                                          } else {

                                          }
                                          // possible to handle other result codes ...
                                      },
                                      Handler())
                }
            } catch (e: IllegalArgumentException) {
                // PixelCopy may throw IllegalArgumentException, make sure to handle it
                e.printStackTrace()
            }
        }

        private fun Bitmap.getTempFileStore(context: Context, subPath: String,
                                       clear: Boolean = true): File? {
            val cacheDir: File = context.cacheDir
            Intrinsics.checkNotNull(cacheDir)
            val directory = File(cacheDir, subPath)
            if (!directory.exists()) directory.mkdirs()
            val type = TypeTempFile.IMAGE_SCREENSHOT_PNG
            if (clear) {
                directory.walkTopDown().filter {
                    it.isFile && it.name.startsWith(type.data.prefix) && it.name.endsWith(type.data.suffix)
                }.forEach { it.delete() }
            }
            val file = File.createTempFile(type.data.prefixWithTime(), type.data.suffix, directory)
            return try {
                FileOutputStream(file).use {
                    this.compress(Bitmap.CompressFormat.PNG, 100, it)
                    it.flush()
                }
                file
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }
        }
        fun Bitmap.getTempFileStoreScreenShot(context: Context,
                                              clear: Boolean = true): File? {
            return this.getTempFileStore(context, SUB_PATH_TEMP_FILE_SCREENSHOT, clear)
        }
        fun Bitmap.getTempFileStoreViewer(context: Context,
                                              clear: Boolean = true): File? {
            return this.getTempFileStore(context, SUB_PATH_TEMP_FILE_VIEWER, clear)
        }


        //region pixel
        fun Int.toPixel() = (this * 192 / 64)
        fun Int.toPixel(context: Context):Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
        //endregion


    }
}