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
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.Actions.RequestStepAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.adapter.CouponListAdapter
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


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
    lateinit var use_point_unitTV:TextView

    lateinit var limit_pointTV: TextView
    lateinit var couponListLV:ListView

    var left_point = ""
    var point = ""
    var use_point = ""
    var new_member_yn = ""
    var step = -1
    var n_left_point = -1
    var member_id = -1
    var type = -1
    var phoneNumber = ""
    var selectedCouponID = -1
    var company_id = -1
    var limitpoint = 0
    var use_point_unit = 0
    var couponData : ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var couponAdapter : CouponListAdapter
    var coupon_yn = "N"

    internal var checkHandler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            checkStep()
        }
    }

    private var timer: Timer? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        phoneNumber = activity!!.intent.extras.getString("phone")
        type = activity!!.intent.extras.getInt("type")
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

        company_id = PrefUtils.getIntPreference(context, "company_id")

        return inflater.inflate(R.layout.fra_coupon, container, false)
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
        couponListLV = view.findViewById(R.id.couponListLV)
        use_point_unitTV =  view.findViewById(R.id.use_point_unitTV)
        limit_pointTV = view.findViewById(R.id.limit_pointTV)


        if(arguments != null) {
            phoneNumber = arguments!!.getString("phoneNumber")
            user_left_point(phoneNumber)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //인텐트로 전화번호를 받는다
        //전화번호로 고객의 정보를 조회하고
        /*val filter = IntentFilter("POINT_USE")
        context!!.registerReceiver(getPhoneNumber, filter)*/

        companyInfo()



        oneLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 1
            l_point()
        }
        twoLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 2
            l_point()
        }
        threeLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 3
            l_point()
        }
        fourLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 4
            l_point()
        }
        fiveLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 5
            l_point()
        }
        sixLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 6
            l_point()
        }
        sevenLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 7
            l_point()
        }
        eightLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 8
            l_point()
        }
        nineLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 9
            l_point()
        }
        zeroLL.setOnClickListener {
            use_pointTV.text = use_pointTV.text.toString() + 0
            l_point()
        }
        backLL.setOnClickListener {
            val text = use_pointTV.text.toString()
            if (use_pointTV.text.toString().length > 0) {
                use_pointTV.text = text.substring(0, text.length - 1)
                if (!text.equals("")) {
                    l_point()
                }
            } else {
                use_pointTV.text = "0"
            }
        }

        couponAdapter = CouponListAdapter(myContext, R.layout.item_member_coupon, couponData)
        couponListLV.adapter = couponAdapter
        couponListLV.setOnItemClickListener { parent, view, position, id ->

            var data = couponData.get(position)

            val check_yn = Utils.getString(data, "check_yn")

            if(check_yn == "Y") {
                coupon_yn="N"
                data.put("check_yn" , "N")
            } else {

                for(i in 0 until couponData.size) {
                    var data = couponData.get(i)
                    coupon_yn="Y"
                    data.put("check_yn", "N")
                }
                coupon_yn="Y"
                data.put("check_yn" , "Y")
            }

            couponAdapter.notifyDataSetChanged()

        }

        useLL.setOnClickListener {

            var usepoint_p = Utils.getString(use_pointTV).replace(",","")
            var mypoint_p = Utils.getString(pointTV).replace(",","")

            var usepoint = usepoint_p.toInt()
            var mypoint = mypoint_p.toInt()
            if (coupon_yn=="N"){
                if (usepoint<limitpoint){
                    Toast.makeText(myContext,"최소사용포인트는 "+limitpoint.toString()+"입니다.",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (usepoint%use_point_unit!=0){
                    Toast.makeText(myContext,"포인트사용단위는 "+use_point_unit.toString()+"입니다.",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (usepoint>mypoint){
                    Toast.makeText(myContext,"포인트 한도초과",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }




            for (i in 0 until couponData.size) {
                val data = couponData[i]
                val check_yn = Utils.getString(data, "check_yn")

                if(check_yn == "Y") {
                    val MemberCoupon = data.getJSONObject("MemberCoupon")
                    selectedCouponID = Utils.getInt(MemberCoupon, "id")
                }

            }

            if (step == 5) {
                step = 7
                changeStep()
            }
        }
        timerStart()
    }


    fun user_left_point(phoneNumber: String) {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("phone", phoneNumber)

        MemberAction.inquiry_point(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        //var point = response.getJSONObject("point")

                        var point = response.getString("point")

                        pointTV.text = Utils.comma(point)
                        left_pointTV.text = Utils.comma(point)

                        couponData.clear()

                        var data = response.getJSONArray("coupons")

                        for (i in 0 until data.length()) {

                            var json = data[i] as JSONObject
                            json.put("check_yn", "N")

                            println("json : " + json)

                            couponData.add(json)
                        }

                        couponAdapter.notifyDataSetChanged()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
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
                        use_point_unit = Utils.getInt(company,"use_point_unit")
                        limitpoint = Utils.getInt(company,"min_use_point")
                        limit_pointTV.text = Utils.comma(limitpoint.toString())+"P 이상부터 사용 가능"
                        use_point_unitTV.text= "포인트사용단위는 "+Utils.comma(use_point_unit.toString())+"P입니다."
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
    fun l_point() {
        point = Utils.getString(pointTV).replace(",","")
        use_point = use_pointTV.text.toString().replace(",","")
        left_point = pointTV.text.toString().replace(",","")

        if (use_point.equals("")) {
            use_point = "0"
        }

        val numpoint = Integer.parseInt(point)
        var n_use_point = Integer.parseInt(use_point)
        n_left_point = Integer.parseInt(left_point)


            n_left_point = numpoint - n_use_point




        pointTV.text = Utils.comma(numpoint.toString())
        use_pointTV.text = Utils.comma(n_use_point.toString())
        left_pointTV.text = Utils.comma(n_left_point.toString())
    }

    // 요청 체크
    fun checkStep() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("phone", phoneNumber)

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
                        member_id = Utils.getInt(member, "id")

                        new_member_yn = Utils.getString(requestStep, "new_member_yn")

                        val result_step = Utils.getInt(requestStep, "step")
                        var point_o = response.getJSONObject("Point")

                        val balance = Utils.getString(point_o, "balance")

                        if (step != result_step) {
                            step = result_step
                            Log.d("스텝", step.toString())
                            pointTV.text = Utils.comma(balance)
                            left_pointTV.text =Utils.comma(balance)
                            if (step == 3) {
                                val intent = Intent(myContext, UseActivity::class.java)
                                type = 4
                                intent.putExtra("type", type)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                            } else if (step == 6) {
                                val intent = Intent(myContext, UseActivity::class.java)
                                type = 4
                                intent.putExtra("type", type)
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
        params.put("company_id", company_id)
        params.put("member_id", member_id)
        params.put("new_member_yn", new_member_yn)
        params.put("member_coupon_id", selectedCouponID)
        params.put("point", use_point.replace(",",""))
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
                        var step = Utils.getInt(requestStep, "step")

//                        if (step != 5) {
//                            use_point = "0"
//                        }

                        if(step == 7) {
                            var intent = Intent()
                            intent.action = "FINISH_ACTIVITY"
                            myContext.sendBroadcast(intent)
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
}
