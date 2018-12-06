package com.devstories.aninuriandroid.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.Actions.RequestStepAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.adapter.FullScreenImageAdapter
import com.devstories.aninuriandroid.base.Config
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
import kotlin.collections.ArrayList

class MainActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var type = -1

    var step = -1
    var member_id = -1
    //적립/사용 타입구분
    var p_type = -1

    var stackpoint = -1
    var company_id = -1

    val USE_ACTIVITY = 301

    lateinit var imageAdater: FullScreenImageAdapter
    var imagePaths = ArrayList<String>()

    internal var checkHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            checkStep()
        }
    }

    private var time = 0
    private var handler: Handler? = null

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.context = this
        progressDialog = ProgressDialog(context)

        company_id = PrefUtils.getIntPreference(context, "company_id")
        //company_id = 1

        var company_name = PrefUtils.getStringPreference(context, "company_name", "")
        if (company_name != null && !company_name.equals("")) {
            storeNameTV.text = company_name
        }

        imageAdater = FullScreenImageAdapter(this, imagePaths)
        imageVP.adapter = imageAdater

        useLL.setOnClickListener {
            if (timer != null) {
                timer!!.cancel()
            }
            type = 1
            val intent = Intent(this, UseActivity::class.java)
            intent.putExtra("type", type)
            startActivity(intent)

        }

        couponLL.setOnClickListener {
            if (timer != null) {
                timer!!.cancel()
            }
            type = 2
            val intent = Intent(this, UseActivity::class.java)
            intent.putExtra("type", type)
            startActivity(intent)
        }

        timerStart()
        companyInfo()
        timer()

    }

    fun companyInfo() {
        val params = RequestParams()
        params.put("company_id", company_id)

        MemberAction.company_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var company = response.getJSONObject("company")
                        val images = response.getJSONArray("images")

                        for(i in 0 until images.length()) {
                            val image:JSONObject = images[i] as JSONObject
                            val companyImage = image.getJSONObject("CompanyImage")

                            val path = Config.url + Utils.getString(companyImage, "image_uri")

                            imagePaths.add(path)

                        }

                        imageAdater.notifyDataSetChanged()

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
//                if (progressDialog != null) {
//                    progressDialog!!.show()
//                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })
    }

    private fun timer() {

        if(handler != null) {
            handler!!.removeCallbacksAndMessages(null);
        }

        handler = object : Handler() {
            override fun handleMessage(msg: Message) {

                time++

                val index = imageVP.getCurrentItem()
                val last_index = imagePaths.size - 1

                if (time % 2 == 0) {
                    if (index < last_index) {
                        imageVP.setCurrentItem(index + 1)
                    } else {
                        imageVP.setCurrentItem(0)
                    }
                }

                handler!!.sendEmptyMessageDelayed(0, 2000) // 1초에 한번 업, 1000 = 1 초
            }
        }
        handler!!.sendEmptyMessage(0)
    }

    // 요청 체크
    fun checkStep() {
        val params = RequestParams()
        params.put("company_id", company_id)

        RequestStepAction.check_step(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                /*if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }*/

                try {

                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var requestStep = response.getJSONObject("RequestStep")
                        var member = response.getJSONObject("Member")

//                        step = Utils.getInt(requestStep, "step")
                        member_id = Utils.getInt(requestStep, "member_id")
                        val result_step = Utils.getInt(requestStep, "step")
                        val new_member_yn = Utils.getString(requestStep, "new_member_yn")

                        if (step != result_step) {

                            step = result_step

                            //포인트적립
                            if (step == 1) {
                                type = 1
                                val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type", type)
                                startActivity(intent)
                            } else if (step == 3) {
                                val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type", 3)
                                intent.putExtra("request_step_id", Utils.getInt(requestStep, "id"))
                                startActivityForResult(intent, USE_ACTIVITY)
                            } else if (step == 4) {
                                type = 1
                                val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type", type)
                                startActivity(intent)
                            } else if (step == 6) {
                                val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type", 3)
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

        })
    }

    // 요청 삭제
    fun endStep(request_step_id:Int) {
        val params = RequestParams()
        params.put("request_step_id", request_step_id)

        RequestStepAction.end_step(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {

                try {

                    val result = response!!.getString("result")
                    if ("ok" == result) {

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

        })
    }

    fun timerStart() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            USE_ACTIVITY -> {
                val request_step_id = data!!.getIntExtra("request_step_id", -1)

                endStep(request_step_id)
            }
        }

    }

}


