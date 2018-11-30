package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class Point_Use_Fragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null


    internal lateinit var view: View
    lateinit var oneLL: LinearLayout
    lateinit var twoLL: LinearLayout
    lateinit var threeLL: LinearLayout
    lateinit var fourLL: LinearLayout
    lateinit var fiveLL: LinearLayout
    lateinit var sixLL: LinearLayout
    lateinit var sevenLL: LinearLayout
    lateinit var eightLL: LinearLayout
    lateinit var nineLL: LinearLayout
    lateinit var zeroLL: LinearLayout
    lateinit var backLL: LinearLayout
    lateinit var useLL: LinearLayout

    lateinit var left_pointTV: TextView
    lateinit var pointTV: TextView
    lateinit var use_pointTV: TextView

    var left_point = ""
    var point = ""
    var use_point = ""
    var new_member_yn = ""
    var step = -1
    var n_left_point = -1
    var member_id = -1
    var type =-1

    internal var checkHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            checkStep()
        }
    }

    private var timer: Timer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        return inflater.inflate(R.layout.fra_coupon,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        useLL = view.findViewById(R.id.useLL)
        backLL = view.findViewById(R.id.backLL)
        oneLL = view.findViewById(R.id.oneLL)
        twoLL = view.findViewById(R.id.twoLL)
        threeLL = view.findViewById(R.id.threeLL)
        fourLL = view.findViewById(R.id.fourLL)
        fiveLL = view.findViewById(R.id.fiveLL)
        sixLL = view.findViewById(R.id.sixLL)
        sevenLL = view.findViewById(R.id.sevenLL)
        eightLL = view.findViewById(R.id.eightLL)
        zeroLL = view.findViewById(R.id.zeroLL)
        nineLL = view.findViewById(R.id.nineLL)
        pointTV = view.findViewById(R.id.pointTV)
        left_pointTV = view.findViewById(R.id.left_pointTV)
        use_pointTV = view.findViewById(R.id.use_pointTV)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //인텐트로 전화번호를 받는다
        //전화번호로 고객의 정보를 조회하고
        user_left_point("")



        oneLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 1)
            l_point()
        }
        twoLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 2)
            l_point()
        }
        threeLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 3)
            l_point()
        }
        fourLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 4)
            l_point()
        }
        fiveLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 5)
            l_point()
        }
        sixLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 6)
            l_point()
        }
        sevenLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 7)
            l_point()
        }
        eightLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 8)
            l_point()
        }
        nineLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 9)
            l_point()
        }
        zeroLL.setOnClickListener {
            use_pointTV.setText(use_pointTV.getText().toString() + 0)
            l_point()
        }
        backLL.setOnClickListener {
            val text = use_pointTV.getText().toString()
            if (use_pointTV.getText().toString().length > 0){
                use_pointTV.setText(text.substring(0, text.length - 1))
                if (!text.equals("")){
                    l_point()
                }
            }else{
                use_pointTV.text = "0"
            }
        }


        useLL.setOnClickListener {
                if (step==5){
                    changeStep()
                }
        }

        timerStart()
    }




    fun user_left_point(phoneNumber:String) {
        val params = RequestParams()
        params.put("company_id", 1)
        params.put("phone", phoneNumber)

        MemberAction.inquiry_point(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        var point = response.getJSONObject("point")
                        var coupon = response.getJSONArray("coupon")

                        var tempPoint = Utils.getString(point, "point")
                        //setText()
                        pointTV.setText(tempPoint)

                        for (i in 0 until coupon.length()) {

                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    responseString: String?,
                    throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                // System.out.println(responseString);
                throwable.printStackTrace()
                error()
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

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    throwable: Throwable,
                    errorResponse: JSONArray?
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




    fun l_point(){
        point = pointTV.text.toString()
        use_point = use_pointTV.text.toString()
        left_point = pointTV.text.toString()

        if (use_point.equals("")){
            use_point = "0"
        }

        val numpoint = Integer.parseInt(point)
        var n_use_point =Integer.parseInt(use_point)
        n_left_point = Integer.parseInt(left_point)


            n_left_point = numpoint - n_use_point

        pointTV.text = numpoint.toString()
        use_pointTV.text = n_use_point.toString()
        left_pointTV.text = n_left_point.toString()
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
                        member_id =  Utils.getInt(member, "member_id")
                        Log.d("아이디",member_id.toString())
                        new_member_yn = Utils.getString(requestStep, "new_member_yn")


                        val result_step = Utils.getInt(requestStep, "step")
                        var point_o = response.getJSONObject("Point")

                        val balance = Utils.getString(point_o, "balance")
                        if(step != result_step) {
                            step = result_step
                            Log.d("스텝",step.toString())
                            pointTV.text = balance
                            left_pointTV.text = balance
                            if (step==3){
                                val intent = Intent(myContext, UseActivity::class.java)
                                type =3
                                intent.putExtra("type",type)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                            }else if (step ==6){
                                val intent = Intent(myContext, UseActivity::class.java)
                                type =3
                                intent.putExtra("type",type)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
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

    // 프로세스
    fun changeStep() {
        val params = RequestParams()
        params.put("company_id", 1)
        params.put("member_id", member_id)
        params.put("new_member_yn", new_member_yn)
        params.put("point", use_point)
        params.put("step", step)

        RequestStepAction.change_step(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var requestStep = response.getJSONObject("RequestStep")
                        var step = Utils.getInt(requestStep,"step")
                    if (step!=5){
                        use_point="0"
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

