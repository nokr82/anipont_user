package com.devstories.aninuriandroid.activities

import android.app.Activity
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.WindowManager
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.*
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_agree.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Dlg_Agree_Activity : RootActivity() {

    lateinit var context: Context
    private var progressDialog:  CustomProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigations(this)
        setContentView(R.layout.dlg_agree)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val url = Config.url + "/agree/view?type=" + 1

        webWV.settings.javaScriptEnabled = true
        webWV.loadUrl(url)
        val url2 = Config.url + "/agree/view2?type=" + 2

        web2WV.settings.javaScriptEnabled = true
        web2WV.loadUrl(url2)

        closeIV.setOnClickListener {
            finish()
        }


    }

    fun hideNavigations(context: Activity) {
        val decorView = context.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        progressDialog?.dismiss()
        progressDialog = null
    }
}
