package com.vsg.helper.common.util.taskAsync

import android.os.AsyncTask

class MakeGenericUnitAsyncTask<TResult> : AsyncTask<Unit, Unit, TResult?>() {
    private var data: TResult? = null
    var onExecute: (() -> TResult)? = null
    var onGetResult: ((TResult?) -> Unit)? = null
    override fun doInBackground(vararg data: Unit?): TResult? {
        this.data = onExecute?.invoke()
        return null
    }

    override fun onPostExecute(result: TResult?) {
        super.onPostExecute(result)
        onGetResult?.invoke(this.data)
    }
}