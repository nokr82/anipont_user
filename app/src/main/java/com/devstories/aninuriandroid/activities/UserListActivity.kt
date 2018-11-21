package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.adapter.UserVisitAdapter
import com.devstories.aninuriandroid.base.RootActivity
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null
    var data = arrayListOf<Int>()


    lateinit var uservisitadapter: UserVisitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        this.context = this
        progressDialog = ProgressDialog(context)


        couponLL.setOnClickListener {
            val intent = Intent(this, PointListActivity::class.java)
            startActivity(intent)
        }
        uservisitLL.setOnClickListener {
            val intent = Intent(this, UservisitActivity::class.java)
            startActivity(intent)
        }
        messageLL.setOnClickListener {
            val intent = Intent(this, MessageManageActivity::class.java)
            startActivity(intent)
        }





        }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }


    }


