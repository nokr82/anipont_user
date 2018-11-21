package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.RootActivity
import kotlinx.android.synthetic.main.activity_user_visit_analysis.*

class UservisitActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>

    var option_amount = arrayOf("5개씩 보기","10개씩 보기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_visit_analysis)

        this.context = this
        progressDialog = ProgressDialog(context)

        adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,option_amount)
        amountSP.adapter = adapter




        }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


