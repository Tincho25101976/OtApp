package com.vsg.helper.ui.widget.datePicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*


class DatePickerFragment(private val date: Date? = null): DialogFragment(), DatePickerDialog.OnDateSetListener {

    var onGetSelectDate:((Date) -> Unit)?= null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c: Calendar = when(date == null) {
            true -> Calendar.getInstance()
            false -> (Calendar.getInstance()).apply { time = date }
        }
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity!!, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        val current = Calendar.getInstance().also { it.set(year, month, day) }.time
        onGetSelectDate?.invoke(current)
    }

    companion object {
        fun newInstance(date: Date?, listener: (Date) -> Unit): DatePickerFragment {
            return DatePickerFragment(date).apply { onGetSelectDate = listener }
        }
    }
}