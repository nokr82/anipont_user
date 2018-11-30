package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.RootActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    var type = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.context = this
        progressDialog = ProgressDialog(context)

        var storeName = PrefUtils.getStringPreference(context, "storeName", "")
        if (storeName != null && !storeName.equals("")) {
            storeNameTV.text = storeName
        }

        useLL.setOnClickListener {
            type = 1
            val intent = Intent(this, UseActivity::class.java)
            intent.putExtra("type", type)
            startActivity(intent)

        }

        couponLL.setOnClickListener {
            type = 2
            val intent = Intent(this, UseActivity::class.java)
            intent.putExtra("type", type)
            startActivity(intent)
        }


        logoLL.setOnClickListener {

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


}


