package com.vsg.helper.ui.data.ui

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
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
abstract class DataBaseActivity<TViewModel, TDao, TEntity>(type: Class<TViewModel>) :
    CurrentBaseActivity<TViewModel, TDao, TEntity>(R.layout.activity_update_database, type),
    IActivityForProgress
        where TViewModel : ViewModelGenericParse<TDao, TEntity>,
              TEntity : IEntityParse<TEntity>,
              TEntity : IDataAdapter,
              TEntity : IResultRecyclerAdapter,
              TEntity : IEntityPagingLayoutPosition,
              TEntity : IEntity,
              TEntity : IIsEnabled,
              TEntity : ItemBase,
              TEntity : IEntityCode,
              TEntity : IEntityCreateDate,
              TEntity : Comparable<TEntity>,
              TDao : IGenericDaoPagingParse<TEntity> {

    //region event
    var onEventSetItem: ((Unit) -> TEntity)? = null
    //endregion

    //region properties
    final override lateinit var tProgressBar: ProgressBar
    final override lateinit var tDescriptionProgress: TextView
    final override lateinit var tLayoutProgress: RelativeLayout
    private lateinit var tListView: ListView
    private lateinit var tLog: TextView
    private lateinit var tSend: ImageView
    private lateinit var crud: ManagerCRUD<DataBaseActivity<TViewModel, TDao, TEntity>, TViewModel, TDao, TEntity>
    private val isCrudLoad: Boolean
        get() = this::crud.isLateinit
    //endregion

    override fun aSetContext(): CurrentBaseActivity<TViewModel, TDao, TEntity> = this
    override fun onExecuteCreate() {
        //region progress
        tProgressBar =
            this.findViewById<ProgressBar>(R.id.activity_update_db_progress_bar)
                .apply {
                    visibility = View.GONE
                }
        tDescriptionProgress = this.findViewById(R.id.activity_update_db_progress_text)
        tLayoutProgress = this.findViewById(R.id.activity_update_db_progress)
        //endregion

        tListView = this.findViewById(R.id.lv_activity_update_database)
        tSend = this.findViewById<ImageView>(R.id.activity_update_iv_send).apply {
            this.setStatus(false)
            this.setOnClickListener {
                val log: String = tLog.toText()
                if (log.isNotEmpty()) {
                    this@DataBaseActivity.sendFile(log)
                }
            }
        }
        tLog = this.findViewById<TextView>(R.id.txt_activity_update_db_log).apply {
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
        this.findViewById<ImageView>(R.id.iv_activity_update_database_find_file_xml)
            .setOnClickListener {
                this.chooserFile(TypeTempFile.DATABASE)
            }
        // read XML
        this.findViewById<ImageView>(R.id.iv_activity_update_database_read_xml)
            .setOnClickListener {
                if (!isCrudLoad) return@setOnClickListener
                set(this.crud.getReadXML())
            }
        // read dataBase
        this.findViewById<ImageView>(R.id.iv_activity_update_database_read_database)
            .setOnClickListener {
                if (!isCrudLoad) return@setOnClickListener
                set(this.crud.getSelect())
            }
        // delete dataBase
        this.findViewById<ImageView>(R.id.iv_activity_update_database_delete_database)
            .setOnClickListener {
                if (!isCrudLoad) return@setOnClickListener
                set(this.crud.getDelete())
            }
        // update dataBase
        this.findViewById<ImageView>(R.id.iv_activity_update_database_update)
            .setOnClickListener {
                HelperProgressBarGetAdapter(this, this.crud).apply {
                    var adapter: UIIDataAdapter<TEntity>?
                    onGetResult = {
                        adapter = it
                        set(adapter)
                    }
                    execute()
                }
            }
        this.onEventExecuteActivityResult = { request, result, data ->
            if (data != null) {
                if (request == REQUEST_FOR_CHOOSER_FILE_FROM_MANAGER && result == Activity.RESULT_OK) {
                    val file: String = this.getTempFileFromUri(
                        data.data,
                        SUB_PATH_TEMP_FILE_DATABASE,
                        TypeTempFile.DATABASE
                    )?.absolutePath
                        ?: ""
                    if (file.isNotEmpty()) {
//                        this.fileXML = file
                        crud =
                            ManagerCRUD(
                                this,
                                aGetEntity(),
                                file
                            )
                    }
                }
            }
        }
    }

    abstract fun aGetEntity(): TEntity
    private fun set(adapter: UIIDataAdapter<TEntity>?) {
        clear()
        if (!isCrudLoad) return
        tLog.text = this.crud.getLog()
        tListView.adapter = adapter
    }

    private fun clear() {
        tLog.text = ""
        tListView.adapter = null
    }
}