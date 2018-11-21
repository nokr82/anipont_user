package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.RootActivity
import kotlinx.android.synthetic.main.activity_message_coupon_management.*

class MessageManageActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null

    lateinit var adapter: ArrayAdapter<String>


    var option_visitday = arrayOf("전체", "5일", "2×4", "4×1", "4×2")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_coupon_management)

        this.context = this
        progressDialog = ProgressDialog(context)


        adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,option_visitday)
        visitdaySP.adapter = adapter


        }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


