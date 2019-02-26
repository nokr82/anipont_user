package com.devstories.aninuriandroid.activities

import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.WindowManager
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.Config
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.RootActivity
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.dlg_agree.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Dlg_Agree_Activity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_agree)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val url = Config.url + "/agree/view?type=" + 1

        webWV.settings.javaScriptEnabled = true
        webWV.loadUrl(url)
        val url2 = Config.url + "/agree/view2?type=" + 2

        web2WV.settings.javaScriptEnabled = true
        web2WV.loadUrl(url2)

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
