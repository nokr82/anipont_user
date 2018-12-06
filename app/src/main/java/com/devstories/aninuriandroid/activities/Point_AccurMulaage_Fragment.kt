package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.aninuriandroid.Actions.RequestStepAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Point_AccurMulaage_Fragment : Fragment() {
    private var progressDialog: ProgressDialog? = null
    lateinit var myContext: Context
    internal lateinit var view: View
    lateinit var useLL: LinearLayout
    lateinit var left_pointTV: TextView
    lateinit var pointTV: TextView
    lateinit var titleTV: TextView

    var type = -1
    private var timer: Timer? = null

    var company_id = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fra_point_accumulate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useLL = view.findViewById(R.id.useLL)

        left_pointTV = view.findViewById(R.id.left_pointTV)
        pointTV = view.findViewById(R.id.pointTV)
        titleTV = view.findViewById(R.id.titleTV)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(myContext, "company_id")

        checkStep()

        useLL.setOnClickListener {

            type = 2

            val intent = Intent(myContext, UseActivity::class.java)
            intent.putExtra("type", type)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        }

        timerStart()

    }

    // 요청 체크
    fun checkStep() {
        val params = RequestParams()
        params.put("company_id", company_id)

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
                        var point_o = response.getJSONObject("Point")

                        val type = Utils.getInt(point_o, "type")
                        val point = Utils.getString(point_o, "point")
                        val balance = Utils.getString(point_o, "balance")

                        Log.d("type", type.toString())

                        if (type == 1) {
                            titleTV.text = "적립완료"
                        } else if (type == 2) {
                            titleTV.text = "사용완료"
                        }

                        left_pointTV.text = balance + "P"
                        pointTV.text = point + "P"

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


    fun timerStart() {
        val task = object : TimerTask() {
            override fun run() {
                var intent = Intent()
                intent.action = "FINISH_ACTIVITY"
                myContext.sendBroadcast(intent)
            }
        }

        timer = Timer()
        timer!!.schedule(task, 2500)
    }


}

