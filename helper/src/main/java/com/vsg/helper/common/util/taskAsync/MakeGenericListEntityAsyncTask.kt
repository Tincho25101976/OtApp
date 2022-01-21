package com.vsg.helper.common.util.taskAsync

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.vsg.helper.common.model.IEntity

class MakeGenericListEntityAsyncTask<TEntity> :
    AsyncTask<Unit, Unit, LiveData<List<TEntity>>?>() where TEntity : IEntity {
    private var data: LiveData<List<TEntity>>? = null
    var onExecute: (() -> LiveData<List<TEntity>>?)? = null
    var onGetResult: ((LiveData<List<TEntity>>?) -> Unit)? = null

    override fun doInBackground(vararg data: Unit?): LiveData<List<TEntity>>? {
        this.data = onExecute?.invoke()
        return null
    }

    override fun onPostExecute(result: LiveData<List<TEntity>>?) {
        super.onPostExecute(result)
        onGetResult?.invoke(this.data)
    }
}