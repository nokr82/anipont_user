package com.devstories.aninuriandroid.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.text.Editable
import android.text.TextWatcher
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
import com.devstories.aninuriandroid.base.CustomProgressDialog
import com.devstories.aninuriandroid.base.HttpClient
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_coupon_use.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fra_use.*
import me.grantland.widget.AutofitTextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class SelectFragment : Fragment() {

    private var progressDialog: CustomProgressDialog? = null
    lateinit var myContext: Context

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
    lateinit var useLL: LinearLayout
    lateinit var backLL: LinearLayout
    lateinit var myPointLL: LinearLayout
    lateinit var pointTV: TextView
    lateinit var phoneTV: TextView
    lateinit var save_pointTV: TextView
    lateinit var typeTV: TextView
    lateinit var couponListLV: ListView
    lateinit var noticeTV: AutofitTextView
    lateinit var fraTitleTV: TextView

    var phone = ""
    var type = -1
    var frame_type = -1
    var save_point = ""
    var step = -1
    var member_id = -1
    var p_type = -1
    var new_member_yn = ""

    var stackpoint = -1

    var company_id = -1

    var couponData : ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var couponAdapter : CouponListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        view = inflater.inflate(R.layout.fra_use, container, false)

        return inflater.inflate(R.layout.fra_use, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        typeTV = view.findViewById(R.id.typeTV)
        save_pointTV = view.findViewById(R.id.save_pointTV)
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
        useLL = view.findViewById(R.id.useLL)
        phoneTV = view.findViewById(R.id.phoneTV)
        noticeTV = view.findViewById(R.id.noticeTV)
        fraTitleTV = view.findViewById(R.id.fraTitleTV)
        couponListLV = view.findViewById(R.id.couponListLV)

        pointTV = view.findViewById(R.id.pointTV)
        myPointLL = view.findViewById(R.id.myPointLL)

        if(arguments != null) {
            frame_type = arguments!!.getInt("type")
            arguments = null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        company_id = PrefUtils.getIntPreference(context, "company_id")


        fraTitleTV.visibility = View.GONE
        kakaoRL.visibility = View.GONE

        save_point = save_pointTV.text.toString()
        typeTV.text = "조회"

        noticeTV.setOnClickListener {
            val intent = Intent(myContext, Dlg_Agree_Activity::class.java)
            startActivity(intent)
        }

        oneLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 1)
        }
        twoLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 2)
        }
        threeLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 3)
        }
        fourLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 4)
        }
        fiveLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 5)
        }
        sixLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 6)
        }
        sevenLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 7)
        }
        eightLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 8)
        }
        nineLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 9)
        }
        zeroLL.setOnClickListener {
            phoneTV.setText(phoneTV.getText().toString() + 0)
        }
        backLL.setOnClickListener {
            val text = phoneTV.getText().toString()
            if (text.length > 0) {
                if (text.length==4){
                    phoneTV.setText(text.substring(0, text.length - 2))
                    Log.d("테스트","33")
                }
                else if (text.length==9){
                    phoneTV.setText(text.substring(0, text.length - 2))
                    Log.d("테스트","77")
                }else{
                    phoneTV.setText(text.substring(0, text.length - 1))
                }

            } else {
            }
        }
        phoneTV.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length==3){
                    phoneTV.setText(phoneTV.getText().toString() + "-")
                    Log.d("테스트","3")
                }
                if (s.length==8){
                    phoneTV.setText(phoneTV.getText().toString() + "-")
                    Log.d("테스트","7")
                }


            }
        })

        useLL.setOnClickListener {

            phone = Utils.getString(phoneTV).replace("-","")

            if (phone == "") {
                Toast.makeText(context, "핸드폰 번호를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            loaduserdata()

        }

        if(frame_type != 2) {
            checkStep()
        } else {

        }

        couponAdapter = CouponListAdapter(myContext, R.layout.item_member_coupon, couponData)
        couponListLV.adapter = couponAdapter

    }

    // 프로세스
    fun changeStep() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("member_id", member_id)
        params.put("new_member_yn", new_member_yn)
        params.put("step", step)
        if (step == 5) {
            if (type == 1) {
                params.put("type", type)
                params.put("point", "use point")
            } else if (type == 2) {
                params.put("type", type)
                params.put("coupon_id", "selectedCouponID")
            }
        }

        RequestStepAction.change_step(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        var requestStep = response.getJSONObject("RequestStep")
                        val result_step = Utils.getInt(requestStep, "step")

                        if(result_step == 2) {
                            val intent = Intent();
                            intent.action = "FINISH_ACTIVITY"
                            myContext.sendBroadcast(intent)
                        } else if (result_step == 5) {
                            val intent = Intent()
                            intent.putExtra("phone", phone)
                            intent.putExtra("type", 2)
                            intent.action = "POINT_USE"
                            myContext.sendBroadcast(intent)
                        }

//                        if (result_step == 2) {
//                            val intent = Intent()
//                            intent.putExtra("phone", phone)
//                            intent.putExtra("type", 2)
//                            intent.action = "POINT_USE"
//                            myContext.sendBroadcast(intent)
//                        }

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {


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
                        val result_step = Utils.getInt(requestStep, "step")
                        member_id = Utils.getInt(requestStep, "member_id")

                        if (step != result_step) {
                            step = result_step

                            if (step == 5) {

                                phonET.setHint("사용할 포인트를 입력하세요.")
                                titleTV.text = "쿠폰/포인트 사용"
                                fraTitleTV.text = "쿠폰/포인트 사용"
                                use_op_LL.visibility = View.GONE

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

    //사용자 회원유무확인
    fun loaduserdata() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("phone", phone)
        params.put("use", "Y")

        MemberAction.is_member(params, object : JsonHttpResponseHandler() {

            @SuppressLint("ResourceType")
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        new_member_yn = Utils.getString(response, "new_member_yn")
                        if (new_member_yn=="Y"){
                            Toast.makeText(myContext,"일치하는 회원이 없습니다.",Toast.LENGTH_SHORT).show()
                            return
                        }
                        if(frame_type != 2) {
                            member_id = Utils.getInt(response, "member_id")

                            if (step == 1) {
                                step = 2
                            } else if (step == 4) {
                                step = 5
                            }

                            changeStep()
                        } else {
                            user_left_point()
                        }


                    } else {
                        Toast.makeText(myContext, "조회실패", Toast.LENGTH_SHORT).show()
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

    fun user_left_point() {
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("phone", phone)

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

                        myPointLL.visibility = View.VISIBLE

                        pointTV.text = Utils.comma(point)

                        couponData.clear()

                        var data = response.getJSONArray("coupons")

                        for (i in 0 until data.length()) {

                            var json = data[i] as JSONObject
                            json.put("check_yn", "N")

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

}
