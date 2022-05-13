package com.vsg.helper.ui.data.ui

import android.provider.Settings.Global.getString
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import com.vsg.helper.R
import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
import com.vsg.helper.common.model.viewModel.ViewModelGenericParse
import com.vsg.helper.common.util.dao.IGenericDaoPagingParse
import com.vsg.helper.helper.HelperUI.Static.REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER
import com.vsg.helper.helper.HelperUI.Static.setStatus
import com.vsg.helper.helper.HelperUI.Static.toText
import com.vsg.helper.helper.file.HelperFile.Static.SUB_PATH_TEMP_FILE_DATABASE
import com.vsg.helper.helper.file.HelperFile.Static.chooserFile
import com.vsg.helper.helper.file.HelperFile.Static.getTempFileFromUri
import com.vsg.helper.helper.file.HelperFile.Static.sendFile
import com.vsg.helper.helper.file.TypeTempFile
import com.vsg.helper.helper.progress.HelperProgressBarGetAdapter
import com.vsg.helper.helper.progress.IActivityForProgress
import com.vsg.helper.ui.adapter.UIIDataAdapter
import com.vsg.helper.ui.util.CurrentBaseActivity

@ExperimentalStdlibApi
abstract class DataBaseActivity<TActivity, TViewModel, TDao, TEntity>(
    item: TEntity,
    val db: IGenericDaoPagingParse<TEntity>,
    activity: TActivity,
    fileSource: String,
    @LayoutRes layout: Int,
    val viewModel: TViewModel? = null
) :
    ManagerCRUD<TActivity, TViewModel, TEntity, TDao>(
        activity,
        item,
        fileSource,
        layout
    ), IActivityForProgress
        where TActivity : CurrentBaseActivity<TViewModel, TDao, TEntity>,
              TViewModel : ViewModelGenericParse<TDao, TEntity>,
              TEntity : IEntityParse<TEntity>,
              TEntity : IDataAdapter,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : IEntityCreateDate,
              TEntity : Comparable<TEntity>,
              TDao : IGenericDaoPagingParse<TEntity> {
//    private var crud: ManagerCRUD<TEntity>? = null

    private lateinit var tListView: ListView
    private lateinit var tLog: TextView
    private lateinit var tSend: ImageView
    override lateinit var tProgressBar: ProgressBar
    override lateinit var tDescriptionProgress: TextView
    override lateinit var tLayoutProgress: RelativeLayout

    override fun onExecuteCreate() {
        activity. makeCustomActionbar(getString(R.string.DialogTextViewCaptionUpdateSource))
        activity.chooserFile(TypeTempFile.DATABASE)

        //region progress
        tProgressBar =
            activity.findViewById<ProgressBar>(R.id.activity_update_db_progress_bar).apply {
                visibility = View.GONE
            }
        tDescriptionProgress = activity.findViewById(R.id.activity_update_db_progress_text)
        tLayoutProgress = activity.findViewById(R.id.activity_update_db_progress)
        //endregion

        tListView = activity.findViewById(R.id.lv_activity_update_database)
        tSend = activity.findViewById<ImageView>(R.id.activity_update_iv_send).apply {
            this.setStatus(false)
            this.setOnClickListener {
                val log: String = tLog.toText()
                if (log.isNotEmpty()) {
                    this@DataBaseActivity.activity.sendFile(log)
                }
            }
        }
        tLog = activity.findViewById<TextView>(R.id.txt_activity_update_db_log).apply {
            movementMethod = ScrollingMovementMethod()
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    tSend.setStatus(!s.isNullOrEmpty())
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    tSend.setStatus(!s.isNullOrEmpty())
                }
            })
        }


        // read XML
        activity.findViewById<ImageView>(R.id.iv_activity_update_database_read_xml)
            .setOnClickListener {
                set(this.getReadXML())
            }
        // read dataBase
        activity.findViewById<ImageView>(R.id.iv_activity_update_database_read_database)
            .setOnClickListener {
                set(this.getSelect())
            }
        // delete dataBase
        activity.findViewById<ImageView>(R.id.iv_activity_update_database_delete_database)
            .setOnClickListener {
                set(this.getDelete())
            }
        // update dataBase
        activity.findViewById<ImageView>(R.id.iv_activity_update_database_update)
            .setOnClickListener {
                HelperProgressBarGetAdapter(this, crud!!).apply {
                    var adapter: UIIDataAdapter<TEntity>?
                    onGetResult = {
                        adapter = it
                        set(adapter)
                    }
                    execute()
                }
            }
        this.activity.onEventExecuteActivityResult = { request, result, data ->
            if (data != null) {
                if (request == REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER && result == RESULT_OK) {
                    val file: String = this.getTempFileFromUri(
                        data.data,
                        SUB_PATH_TEMP_FILE_DATABASE,
                        TypeTempFile.DATABASE
                    )?.absolutePath
                        ?: ""
                    if (file.isNotEmpty()) {
                        crud = ManagerCRUD(this, this.getDatabaseName(), item, file, db)
                    }
                }
            }
        }
    }

    private fun set(adapter: UIIDataAdapter<TEntity>?) {
        clear()
        tLog.text = this.getLog()
        tListView.adapter = adapter
    }

    private fun clear() {
        tLog.text = ""
        tListView.adapter = null
    }
}