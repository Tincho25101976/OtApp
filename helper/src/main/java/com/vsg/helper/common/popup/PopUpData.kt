package com.vsg.helper.common.popup

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import android.text.Spanned
import androidx.annotation.DrawableRes
import com.vsg.helper.helper.string.HelperString

class PopUpData(override var title: String, override var body: String) : IPopUpData, Parcelable {
    @DrawableRes
    override var icon: Int = 0
    override var bitmap: Bitmap? = null
    override val isBitmap: Boolean
        get() = bitmap != null && bitmap!!.byteCount > 0
    override var factorWidth: Double = 0.8
        get() {
            if (field <= 0) field = 0.1
            if (field >= 1) field = 0.9
            return field
        }
    override var factorHeight: Double = 0.8
        get() {
            if (field <= 0) field = 0.1
            if (field >= 1) field = 0.9
            return field
        }
    override var commandOK: Boolean = false

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeString(title)
            dest.writeString(body)
            dest.writeInt(icon)
            dest.writeDouble(factorWidth)
            dest.writeDouble(factorHeight)
        }
    }

    override fun describeContents(): Int = 0
    override fun isSpanned(): Boolean = this.toHtml.isNotEmpty()
    override var toHtml: Spanned = HelperString.defaultSpanned()
    override var canceledOnTouchOutside: Boolean = false

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PopUpData> =
            object : Parcelable.Creator<PopUpData> {
                override fun newArray(size: Int): Array<PopUpData?> = arrayOfNulls(size)
                override fun createFromParcel(source: Parcel): PopUpData = PopUpData(source)
            }
        const val FACTOR: Double = 0.8
    }

    constructor(source: Parcel) : this(
        source.readString().toString(),
        source.readString().toString()
    ) {
        this.icon = source.readInt()
        this.factorWidth = source.readDouble()
        this.factorHeight = source.readDouble()
    }
}