package com.vsg.helper.common.util.taskAsync

import android.os.AsyncTask
import com.vsg.helper.common.model.IEntity

class MakeGenericEntityAsyncTask<TEntity, TResult> :
    AsyncTask<TEntity, Unit, TResult?>() where TEntity : IEntity {
    private var data: TResult? = null
    var onExecute: ((TEntity) -> TResult)? = null
    var onGetResult: ((TResult?) -> Unit)? = null
    override fun doInBackground(vararg data: TEntity?): TResult? {
        data.filterNotNull().first().also {
            this.data = onExecute?.invoke(it)
        }
        return null
    }

    override fun onPostExecute(result: TResult?) {
        super.onPostExecute(result)
        onGetResult?.invoke(this.data)
    }
}