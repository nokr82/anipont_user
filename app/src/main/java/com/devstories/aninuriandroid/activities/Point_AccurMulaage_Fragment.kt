package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.devstories.aninuriandroid.R
import kotlinx.android.synthetic.main.fra_point_accumulate.*


class Point_AccurMulaage_Fragment : Fragment() {
    private var progressDialog: ProgressDialog? = null
    lateinit var myContext: Context
    internal lateinit var view: View
    lateinit var useLL: LinearLayout
    var type =-1

    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fra_point_accumulate,container,false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useLL = view.findViewById(R.id.useLL)



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        useLL.setOnClickListener {
            type = 2
            val intent = Intent(myContext, UseActivity::class.java)
            intent.putExtra("type",type)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        }


        }

    }

