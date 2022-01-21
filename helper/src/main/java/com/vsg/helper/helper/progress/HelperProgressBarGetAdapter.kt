package com.vsg.helper.helper.progress

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.GradientDrawable
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.helper.font.FontManager.Static.typeFaceCustom
import com.vsg.helper.ui.adapter.UIIDataAdapter
import com.vsg.helper.R
import java.text.DecimalFormat
import kotlin.random.Random

class HelperProgressBarGetAdapter<TActivity, TData>(private val activity: TActivity,
                                                    private val process: ICallbackProcessGetAdapter<TData>
) :
    AsyncTask<Unit, Int, Int>() where TData : IDataAdapter, TActivity : Activity, TActivity : IActivityForProgress {

    var onGetResult: ((UIIDataAdapter<TData>?) -> Unit)? = null
    var data: UIIDataAdapter<TData>? = null
    var resizeValue: Double = 0.6
        get() {
            if (field <= 0) field = 0.1
            if (field > 1) field = 1.0
            return field
        }

    init {
        setVisibility(false)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val width = (dm.widthPixels * resizeValue).toInt()
        activity.tLayoutProgress.apply {
            layoutParams.apply {
                this.width = width
                this.height = RelativeLayout.LayoutParams.WRAP_CONTENT
                (this as RelativeLayout.LayoutParams).apply {
                    addRule(RelativeLayout.CENTER_IN_PARENT)
                }
            }
            setBackgroundResource(R.drawable.shadow_view)
            elevation = 100F
            bringToFront()
        }
        activity.tProgressBar.apply {
            isIndeterminate = false
            min = 0
            scaleY = 1.0F

            val array = (1..8).map { 10F }.toFloatArray()
            val gradient =
                GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, getColors(6)).apply {
                    this.cornerRadii = array
                    this.gradientType = GradientDrawable.LINEAR_GRADIENT
                    this.alpha = 150
                }
            progressDrawable = ClipDrawable(gradient, Gravity.START, ClipDrawable.HORIZONTAL)
            layoutParams = getCustomLayout(this, RelativeLayout.CENTER_IN_PARENT).apply {
                topMargin = 0
                bottomMargin = 20
            }
        }
        activity.tDescriptionProgress.apply {
            gravity = Gravity.CENTER
            textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            textSize = 20F
            maxLines = 1
            setSingleLine()
            typeface = activity.typeFaceCustom(Typeface.BOLD_ITALIC)

            val layout = getCustomLayout(this, RelativeLayout.CENTER_HORIZONTAL)
            layout.addRule(RelativeLayout.BELOW, activity.tProgressBar.id)
            layout.topMargin = 40
            layoutParams = layout
        }
        setVisibility(true)
    }

    override fun doInBackground(vararg params: Unit?): Int {
        process.apply {
            onProgress = { v, m, p ->
                activity.tProgressBar.max = m
                publishProgress(v)
                activity.tDescriptionProgress.text = StringBuilder().append(p.format()).append(" %")
            }
            data = this.processGetAdapter()
        }
        return 0
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        activity.tProgressBar.progress = values[0]!!
    }

    override fun onPostExecute(result: Int?) {
        super.onPostExecute(result)
        setVisibility(false)
        Toast.makeText(activity, "Proceso completo!!!", Toast.LENGTH_SHORT).show()
        onGetResult?.invoke(data)
    }

    private fun getCustomLayout(view: View, rule: Int): RelativeLayout.LayoutParams {
        val layout: RelativeLayout.LayoutParams = view.layoutParams as RelativeLayout.LayoutParams
        layout.addRule(rule)
        layout.setMargins(DEFAULT_MARGIN, DEFAULT_MARGIN, DEFAULT_MARGIN, DEFAULT_MARGIN)
        layout.height = ViewGroup.LayoutParams.WRAP_CONTENT
        layout.width = ViewGroup.LayoutParams.MATCH_PARENT
        return layout
    }

    private fun setVisibility(visible: Boolean) {
        val status = when (visible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        activity.tLayoutProgress.visibility = status
        activity.tProgressBar.visibility = status
        activity.tDescriptionProgress.visibility = status
    }

    private fun getRandomInt(): Int {
        var result: Int
        do {
            result = Random.nextInt(0, Random.nextInt(1, 256))
        } while (result <= 0 || result > 256)
        return result
    }

    private fun randomColor(): Int {
        //(Math.random() * 16777215).toInt() or (0xFF shl 24)
        return Color.argb(255, getRandomInt(), getRandomInt(), getRandomInt())
    }

    private fun getColors(colors: Int = 4): IntArray {
        var qty = colors
        if (colors <= 0) qty = 2
        if (colors > 10) qty = 10
        val data: MutableList<Int> = mutableListOf()
        do {
            val temp = randomColor()
            if (!data.any { it == temp }) data.add(temp)
        } while (data.count() < qty)
        return data.toIntArray()
    }

    private fun getDefaultColors() =
        intArrayOf(0xFF4175A4.toInt(), 0xFF47A422.toInt(), 0xFFD9DC76.toInt(), 0xFFDC0A26.toInt())

    private fun Number.format(decimalPlace: Int = 2): String {
        val df = DecimalFormat()
        df.maximumFractionDigits = decimalPlace
        df.minimumFractionDigits = decimalPlace
        return df.format(this)
    }

    companion object Static {
        const val DEFAULT_MARGIN: Int = 10
    }
}