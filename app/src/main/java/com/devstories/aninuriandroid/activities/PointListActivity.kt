package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.adapter.UserListAdapter
import com.devstories.aninuriandroid.base.RootActivity
import kotlinx.android.synthetic.main.activity_point_list.*

class PointListActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null

    var data = arrayListOf<Int>()


    lateinit var useradapter: UserListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_list)

        this.context = this
        progressDialog = ProgressDialog(context)


        for (i in 0..5) {
            data.add(i)
        }

        useradapter = UserListAdapter(this,data)
        userLV.adapter = useradapter


        }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }




    }


    }


