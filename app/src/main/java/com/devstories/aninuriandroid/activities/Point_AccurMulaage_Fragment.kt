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
import android.widget.Toast
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.Actions.RequestStepAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_point_accumulate.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Point_AccurMulaage_Fragment : Fragment() {
    private var progressDialog: ProgressDialog? = null
    lateinit var myContext: Context
    internal lateinit var view: View
    lateinit var left_pointTV: TextView
    lateinit var pointTV: TextView
    lateinit var titleTV: TextView

    var point =-1
    var balance = -1
    var member_point = ""
 var stack_point = -1
    var member_id = ""


    var type = -1
    private var timer: Timer? = null

    var company_id = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fra_point_accumulate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        left_pointTV = view.findViewById(R.id.left_pointTV)
        pointTV = view.findViewById(R.id.pointTV)
        titleTV = view.findViewById(R.id.titleTV)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(myContext, "company_id")

        checkStep()

      /*  useLL.setOnClickListener {

            type = 2

            val intent = Intent(myContext, UseActivity::class.java)
            intent.putExtra("type", type)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        }*/

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

                        Log.d("결과",response.toString())

                        member_id = Utils.getString(member, "id")

                        type = Utils.getInt(point_o, "type")
                         point = Utils.getInt(point_o, "point")
                         balance = Utils.getInt(point_o, "balance")
                        stack_point = Utils.getInt(point_o, "stack_point")
                        Log.d("type", type.toString())

                        if (balance<100){
                            membership_point()
                        }


                        if (stack_point != -1){
                            stack_pointTV.visibility = View.VISIBLE
                            stack_titleTV.visibility = View.VISIBLE
                            stack_pointTV.text =Utils.comma(stack_point.toString()) + "P"
                        }


                        if (type == 1) {
                            stack_pointTV.visibility = View.GONE
                            stack_titleTV.visibility = View.GONE
                            titleTV.text = "적립완료"
                        } else if (type == 2) {
                            titleTV.text = "사용완료"
                        }
                        send_alram()
                        left_pointTV.text = Utils.comma(balance.toString()) + "P"
                        pointTV.text = Utils.comma(point.toString()) + "P"

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

    fun membership_point() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", member_id)

        MemberAction.membership_point(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")
                    val membership_point  = response!!.getString("membership_point")
                    Log.d("membership",membership_point)
                    if ("ok" == result) {
                        Toast.makeText(myContext,"멤버쉽결제로 인한"+Utils.comma(membership_point)+"P가 적립되었습니다.",Toast.LENGTH_SHORT).show()

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

    // 알람톡보내기
    fun send_alram() {
        val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA)
        val str_date = df.format(Date())

        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", member_id)
        params.put("use_point", Utils.comma(point.toString()))
        params.put("stack_point", Utils.comma(point.toString()))
        params.put("left_point", Utils.comma(balance.toString()))
        params.put("point", Utils.comma((balance+point).toString()))
        params.put("type", type)

        RequestStepAction.send_alram(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

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
                intent.action = "END_STEP"
                myContext.sendBroadcast(intent)
            }
        }

        timer = Timer()
        timer!!.schedule(task, 5000)
    }


}

