package com.vsg.helper.helper.bitmap

import android.app.Activity
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import java.io.ByteArrayOutputStream
import java.util.*


class HelperBitmap {
    companion object {
        fun Bitmap.grayScale(): Bitmap {
//            val mutable = this.copy(Bitmap.Config.ARGB_8888, true)
            val result = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
            val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
            val paint = Paint().apply { colorFilter = colorMatrixFilter }
            canvas.drawBitmap(this, 0f, 0f, paint)
            return result
        }

//        fun Bitmap.grayScale(): Bitmap {
//            val mutable = this.copy(Bitmap.Config.ARGB_8888, true)
//            val result = Bitmap.createBitmap(mutable.width, mutable.height, Bitmap.Config.ARGB_8888)
//            val canvas = Canvas(result)
//            val paint = Paint()
//            val colorMatrix = ColorMatrix()
//            colorMatrix.setSaturation(0f)
//            val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
//            paint.colorFilter = colorMatrixFilter
//            canvas.drawBitmap(mutable, 0F, 0F, paint)
//            return result
//        }

        fun Bitmap?.encodeToString(): String? {
            if (this == null) return null
            var result: String?
            ByteArrayOutputStream().use {
                this.compress(Bitmap.CompressFormat.PNG, 100, it)
                result = Base64.getEncoder().encodeToString(it.toByteArray())
            }
            return result
        }

        fun Bitmap?.toArray(): ByteArray? {
            if (this == null || this.byteCount <= 0) return null
            var result: ByteArray?
            ByteArrayOutputStream().use {
                this.compress(Bitmap.CompressFormat.PNG, 100, it)
                result = it.toByteArray()
                it.flush()
            }
            return result
        }

        fun Bitmap?.toScale(value: Int): Bitmap? {
            if (this == null) return null
            return try {
                if (height >= width) {
                    if (height <= value) return this
                    val aspectRatio = width.toDouble() / height.toDouble()
                    val newWidth = (value * aspectRatio).toInt()
                    Bitmap.createScaledBitmap(this, newWidth, value, false)
                } else {
                    if (width <= value) return this
                    val aspectRatio = height.toDouble() / value.toDouble()
                    val newHeight = (value * aspectRatio).toInt()
                    Bitmap.createScaledBitmap(this, value, newHeight, false)
                }
            } catch (e: Exception) {
                this
            }
        }

        fun Activity.toBitmap(@DrawableRes pic: Int): Bitmap {
            val decoder = BitmapFactory.decodeResource(this.resources, pic)
            return decoder.copy(Bitmap.Config.ARGB_8888, true)
        }

        fun Drawable.drawableToBitmap(): Bitmap? {
            if (this is BitmapDrawable) {
                if (this.bitmap != null) return this.bitmap
            }
            val bitmap: Bitmap =
                if (this.intrinsicWidth <= 0 || this.intrinsicHeight <= 0) {
                    Bitmap.createBitmap(
                        1,
                        1,
                        Bitmap.Config.ARGB_8888
                    ) // Single color bitmap will be created of 1x1 pixel
                } else {
                    Bitmap.createBitmap(
                        this.intrinsicWidth,
                        this.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                }
            val canvas = Canvas(bitmap)
            this.setBounds(0, 0, canvas.width, canvas.height)
            this.draw(canvas)
            return bitmap
        }

        fun String.toBitmap(textSize: Float, textColor: Int): Bitmap? {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.textSize = textSize
                this.color = textColor
                this.textAlign = Paint.Align.LEFT
            }
            val baseline = -paint.ascent() // ascent() is negative
            val width = (paint.measureText(this) + 0.5F).toInt() // round
            val height = (baseline + paint.descent() + 0.5F).toInt()
            val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawText(this, 0F, baseline, paint)
            return image
        }

        fun Bitmap.drawText(
            text: String,
            textSize: Float = 55F,
            color: Int = Color.RED,
            typeface: Typeface = Typeface.DEFAULT_BOLD
        ): Bitmap? {
            val bitmap = copy(config, true)
            val canvas = Canvas(bitmap)
            Paint().apply {
                this.flags = Paint.ANTI_ALIAS_FLAG
                isAntiAlias = true
                this.color = color
                this.textSize = textSize
                this.typeface = typeface
                this.setShadowLayer(1f, 0f, 1f, Color.WHITE)
                canvas.drawText(text, 20f, height - 20f, this)
            }
            return bitmap
        }

        fun String.toBitmap(
            height: Int,
            width: Int,
            textSize: Float = 55F,
            color: Int = Color.RED,
            background: Int = Color.LTGRAY,
            typeface: Typeface = Typeface.DEFAULT_BOLD

        ): Bitmap? {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(background)
            return bitmap.drawText(this, textSize, color, typeface)
        }

        //region rotation
        fun toMapRotate(parts: Int = 4): MutableList<Int> {
            val module: Int = 360 / when (parts < 2) {
                true -> 2
                false -> parts
            }.toInt()
            return (1..parts).map { it * module }.toMutableList()
        }

        fun Activity.makeMapRotate(rotates: MutableList<Int>): MutableList<Pair<Int, Float>> {
            var i = 0
            if (rotates.any { it == 0 }) rotates.removeAll { it == 0 }
            if (rotates.any { it == 360 }) rotates.removeAll { it == 360 }
            val mapRotate: MutableList<Pair<Int, Float>> = mutableListOf()
            mapRotate.add(Pair(i++, 0F))
            rotates.filter { it in (0..360) }.groupBy { it }.map { it.key }.sortedBy { it }
                .forEach { mapRotate.add(Pair(i++, it.toFloat())) }
            return mapRotate
        }

        fun Activity.makeMapRotate(parts: Int = 4): MutableList<Pair<Int, Float>> =
            this.makeMapRotate(
                toMapRotate(parts)
            )

        fun Int.nextAngle(map: MutableList<Pair<Int, Float>>): Int {
            var temp: Int = this
            return when (temp >= map.maxOf { it.first }) {
                true -> map.minOf { it.first }
                false -> ++temp
            }
        }

        fun Int.nextRotation(map: MutableList<Pair<Int, Float>>): Pair<Int, Float> {
            val angle = this.nextAngle(map)
            val rotation: Float = map.first { it.first == this }.second
            return Pair(angle, rotation)
        }

        fun Bitmap?.toRotate(rotation: Float): Bitmap? {
            if (this == null) return null
            if (rotation == 0F || rotation % 360 == 0F) return this
            val matrix = Matrix().apply { postRotate(rotation) }
            val rotated = Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
            return when (rotated == null) {
                true -> rotated
                false -> rotated.copy(Bitmap.Config.ARGB_8888, true)
            }
        }
        //endregion
    }
}