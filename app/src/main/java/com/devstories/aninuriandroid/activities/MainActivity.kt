package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.devstories.aninuriandroid.Actions.RequestStepAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.RootActivity
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : RootActivity() {

    lateinit var context:Context
    private var progressDialog: ProgressDialog? = null


    var type = -1

    var step = -1
    var member_id = -1
    //적립/사용 타입구분
    var p_type = -1

    var stackpoint = -1

    internal var checkHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            checkStep()
        }
    }

    private var timer: Timer? = null


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
            if (timer != null) {
                timer!!.cancel()
            }
            type = 1
            val intent = Intent(this, UseActivity::class.java)
            intent.putExtra("type",type)
            startActivity(intent)

        }

        couponLL.setOnClickListener {
            if (timer != null) {
                timer!!.cancel()
            }
            type = 2
            val intent = Intent(this, UseActivity::class.java)
            intent.putExtra("type",type)
            startActivity(intent)
        }

        timerStart()




        }

    // 요청 체크
    fun checkStep() {
        val params = RequestParams()
        params.put("company_id", 1)

        RequestStepAction.check_step(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var requestStep = response.getJSONObject("RequestStep")
                        var member = response.getJSONObject("Member")

//                        step = Utils.getInt(requestStep, "step")
                        member_id = Utils.getInt(requestStep, "member_id")
                        val result_step = Utils.getInt(requestStep, "step")
                        val new_member_yn = Utils.getString(requestStep, "new_member_yn")
                        if(step != result_step) {
                            step = result_step
                            Log.d("스텝",step.toString())
                            //포인트적립
                            if(step == 1) {
                                type = 1
                              val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type",type)
                                startActivity(intent)

                            }//포인트 사용
                            else if(step == 4){
                                type = 1
                                val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type",type)
                                startActivity(intent)

                            }

                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }


            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }


            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONObject?
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {
                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    fun timerStart(){
        val task = object : TimerTask() {
            override fun run() {
                checkHandler.sendEmptyMessage(0)
            }
        }

        timer = Timer()
        timer!!.schedule(task, 0, 2000)
    }




    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        if (timer != null) {
            timer!!.cancel()
        }


    }


    }


