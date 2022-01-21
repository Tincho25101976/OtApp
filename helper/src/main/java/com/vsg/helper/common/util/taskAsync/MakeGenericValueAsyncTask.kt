package com.vsg.helper.common.util.taskAsync

import android.os.AsyncTask

class MakeGenericValueAsyncTask<TValueIn, TResult> : AsyncTask<TValueIn, Unit, TResult?>() {
    private var data: TResult? = null
    var onExecute: ((TValueIn) -> TResult)? = null
    var onGetResult: ((TResult?) -> Unit)? = null
    override fun doInBackground(vararg data: TValueIn?): TResult? {
        val temp = data.firstOrNull() ?: return null
        this.data = onExecute?.invoke(temp)
        return null
    }
    override fun onPostExecute(result: TResult?) {
        super.onPostExecute(result)
        onGetResult?.invoke(this.data)
    }
}