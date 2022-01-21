package com.vsg.helper.helper.progress

import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

interface IActivityForProgress {
    var tProgressBar: ProgressBar
    var tDescriptionProgress: TextView
    var tLayoutProgress: RelativeLayout
}