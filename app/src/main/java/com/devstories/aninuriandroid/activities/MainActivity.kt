package com.devstories.aninuriandroid.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.Actions.RequestStepAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.adapter.FullScreenImageAdapter
import com.devstories.aninuriandroid.base.*
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
    private var progressDialog:  CustomProgressDialog? = null

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
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ")
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.")
        }
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        hideNavigations(this)

        setContentView(R.layout.activity_main)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        company_id = PrefUtils.getIntPreference(context, "company_id")
        //company_id = 1

        var company_name = PrefUtils.getStringPreference(context, "company_name", "")
        if (company_name != null && !company_name.equals("")) {
            storeNameTV.text = company_name
        }

        imageAdater = FullScreenImageAdapter(this, imagePaths)
        imageVP.adapter = imageAdater


        logoutTV.setOnClickListener {
            val builder = AlertDialog.Builder(context)
                builder.setMessage("정말 로그아웃 하시겠습니까?")
            var yes = "예"
            var no ="아니요"
            builder
                    .setPositiveButton(yes, DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                        PrefUtils.clear(context)
                        val intent = Intent(context, LoginActivity::class.java)
                        PrefUtils.setPreference(context,"autoLogin", false)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    })
                    .setNegativeButton(no, DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })


            val alert = builder.create()
            alert.show()

        }


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
//            if (timer != null) {
//                timer!!.cancel()
//            }

            val intent = Intent(this, UseActivity::class.java)
            intent.putExtra("type", 2)
            startActivity(intent)

        }

        timerStart()
        companyInfo()
        timer()

    }

    override fun onResume() {
        super.onResume()
        hideNavigations(this)
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
                        Log.d("이미지",images.toString())
                        imagePaths.clear()
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
                        imageVP.setCurrentItem(0,false)
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
                                intent.putExtra("type", 4)
                                intent.putExtra("request_step_id", Utils.getInt(requestStep, "id"))
                                startActivityForResult(intent, USE_ACTIVITY)
                            } else if (step == 4) {
                                type = 3
                                val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type", type)
                                startActivity(intent)
                            } else if (step == 6) {
                                val intent = Intent(context, UseActivity::class.java)
                                intent.putExtra("type", 4)
                                intent.putExtra("request_step_id", Utils.getInt(requestStep, "id"))
                                startActivityForResult(intent, USE_ACTIVITY)
                            }else if (step == 9) {
                                val intent = Intent(context, ContractWriteActivity::class.java)
                                intent.putExtra("contract_id", Utils.getInt(requestStep, "contract_id"))
                                intent.putExtra("request_step_id", Utils.getInt(requestStep, "id"))
                               startActivity(intent)
                            }else if (step == 8){
                                val intent = Intent(context, UseActivity::class.java)
                                startActivity(intent)
                            }

                        }

                    } else {
                        step  = -1
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


