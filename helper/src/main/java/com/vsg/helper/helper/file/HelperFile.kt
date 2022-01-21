package com.vsg.helper.helper.file

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.vsg.helper.helper.HelperUI.Static.REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER
import com.vsg.helper.helper.permission.HelperPerminission.Static.checkedPermissionStorage
import com.vsg.helper.helper.screenshot.HelperScreenShot.Static.SUB_PATH_TEMP_FILE_CAMERA
import java.io.File
import java.io.FileOutputStream
import kotlin.jvm.internal.Intrinsics


class HelperFile {
    companion object Static {
        const val SUB_PATH_TEMP_FILE_DATABASE = "tempDataBase"
        const val SUB_PATH_TEMP_CHOOSER_FILE = "tempChooserFile"

        private fun appProvider(app: String): String = "$app.provider"

        fun Activity.getURI(file: File): Uri =
            FileProvider.getUriForFile(this, appProvider(this.packageName), file)

        fun Activity.getURI(file: String): Uri =
            FileProvider.getUriForFile(this, appProvider(this.packageName), File(file))

        fun Activity.sendFile(file: File?) {
            if (file != null && file.exists()) {
                try {
                    val uri: Uri = getURI(file)
                    val s = Intent(Intent.ACTION_SEND)
                    s.type = this.contentResolver.getType(uri)
                    s.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    s.putExtra(Intent.EXTRA_STREAM, uri)
                    this.startActivity(Intent.createChooser(s, "Compartir"))
                } catch (e: Exception) {
                }
            }
        }

        fun Activity.sendFile(file: String) {
            if (file.isEmpty()) return
            try {
                val s = Intent(Intent.ACTION_SEND)
                s.type = "text/plain"
                s.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                s.putExtra(Intent.EXTRA_TEXT, file)
                this.startActivity(Intent.createChooser(s, "Compartir"))
            } catch (e: Exception) {
            }
        }

        fun Activity.chooserFile(type: TypeTempFile) {
            if (!this.checkedPermissionStorage()) return
            val file: File =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val s = Intent(Intent.ACTION_GET_CONTENT)
            s.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            s.setDataAndType(this.getURI(file), type.data.type())
            this.startActivityForResult(
                Intent.createChooser(s, "Open"),
                REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER
            )
        }

        fun Activity.getTempFileFromUri(
            uri: Uri?,
            subPath: String,
            type: TypeTempFile,
            clear: Boolean = true
        ): File? {
            if (uri == null) return null
            val directory = when (subPath.isNotEmpty()) {
                true -> File(this.cacheDir, subPath)
                false -> this.cacheDir
            }
            if (!directory.exists()) directory.mkdirs()
            if (clear) {
                directory.walkTopDown().filter {
                    it.isFile && it.name.startsWith(type.data.prefix) && it.name.endsWith(type.data.suffix)
                }.forEach { it.delete() }
            }
            val file = File.createTempFile(type.data.prefix, type.data.suffix, directory)
            FileOutputStream(file).use {
                this.contentResolver.openInputStream(uri)?.copyTo(it)
            }
            return file
        }

        private fun Activity.getTempFileStore(
            subPath: String,
            type: TypeTempFile,
            clear: Boolean = true
        ): File? {
            val cacheDir: File = this.cacheDir
            Intrinsics.checkNotNull(cacheDir)
            val directory = File(cacheDir, subPath)
            if (!directory.exists()) directory.mkdirs()
            if (clear) {
                directory.walkTopDown().filter {
                    it.isFile && it.name.startsWith(type.data.prefix) && it.name.endsWith(type.data.suffix)
                }.forEach { it.delete() }
            }
            return File.createTempFile(type.data.prefixWithTime(), type.data.suffix, directory)
        }

        fun Activity.getTempUriForCamera(clear: Boolean = true): Uri? {
            val file: File =
                this.getTempFileStore(SUB_PATH_TEMP_FILE_CAMERA, TypeTempFile.IMAGE_CAMERA, clear)
                    ?: return null
            return this.getURI(file)
        }

        fun Activity.getTempFileForCamera(clear: Boolean = true): File? {
            return this.getTempFileStore(SUB_PATH_TEMP_FILE_CAMERA, TypeTempFile.IMAGE_CAMERA, clear)
                ?: return null
        }

        fun String.getMimeType(): String =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(this) ?: "*/*"
    }
}