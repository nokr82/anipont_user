package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.RootActivity

class CouponActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_search)

        this.context = this
        progressDialog = ProgressDialog(context)





        }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


