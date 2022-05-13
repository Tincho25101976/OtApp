package com.vsg.ot.ui.data

import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
import com.vsg.helper.common.adapter.IDataAdapter
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.*
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
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import com.vsg.ot.common.model.init.dao.DaoGenericOtParse

@ExperimentalStdlibApi
class DataBaseActivity<T>(val item: T, val db: DaoGenericOtParse<T>) :
    BaseActivity(R.layout.activity_update_database), IActivityForProgress
        where T : IEntityParse<T>,
              T : IDataAdapter,
              T : IResultRecyclerAdapter,
              T : IEntityPagingLayoutPosition,
              T : IEntity,
              T : IIsEnabled,
              T : IEntityCreateDate,
              T : Comparable<T> {
    private var crud: ManagerCRUD<T>? = null
    private lateinit var tListView: ListView
    private lateinit var tLog: TextView
    private lateinit var tSend: ImageView
    override lateinit var tProgressBar: ProgressBar
    override lateinit var tDescriptionProgress: TextView
    override lateinit var tLayoutProgress: RelativeLayout

    override fun onExecuteCreate() {
        makeCustomActionbar(getString(R.string.DialogTextViewCaptionUpdateSource))
        this.chooserFile(TypeTempFile.DATABASE)

        //region progress
        tProgressBar = findViewById<ProgressBar>(R.id.activity_update_db_progress_bar).apply {
            visibility = View.GONE
        }
        tDescriptionProgress = findViewById(R.id.activity_update_db_progress_text)
        tLayoutProgress = findViewById(R.id.activity_update_db_progress)
        //endregion

        tListView = findViewById(R.id.lv_activity_update_database)
        tSend = findViewById<ImageView>(R.id.activity_update_iv_send).apply {
            this.setStatus(false)
            this.setOnClickListener {
                if (crud != null) {
                    val log: String = tLog.toText()
                    if (log.isNotEmpty()) {
                        this@DataBaseActivity.sendFile(log)
                    }
                }
            }
        }
        tLog = findViewById<TextView>(R.id.txt_activity_update_db_log).apply {
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
        findViewById<ImageView>(R.id.iv_activity_update_database_read_xml).setOnClickListener {
            set(crud?.getReadXML())
        }
        // read dataBase
        findViewById<ImageView>(R.id.iv_activity_update_database_read_database).setOnClickListener {
            set(crud?.getSelect())
        }
        // delete dataBase
        findViewById<ImageView>(R.id.iv_activity_update_database_delete_database).setOnClickListener {
            set(crud?.getDelete())
        }
        // update dataBase
        findViewById<ImageView>(R.id.iv_activity_update_database_update).setOnClickListener {
            HelperProgressBarGetAdapter(this, crud!!).apply {
                var adapter: UIIDataAdapter<T>?
                onGetResult = {
                    adapter = it
                    set(adapter)
                }
                execute()
            }
        }
        this.onEventExecuteActivityResult = { request, result, data ->
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

    private fun set(adapter: UIIDataAdapter<T>?) {
        clear()
        if (crud == null) return
        tLog.text = crud?.getLog()
        tListView.adapter = adapter
    }

    private fun clear() {
        tLog.text = ""
        tListView.adapter = null
    }
}