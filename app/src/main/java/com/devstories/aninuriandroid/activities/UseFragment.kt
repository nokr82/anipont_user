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
import com.devstories.aninuriandroid.base.HttpClient
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_coupon_use.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class UseFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null
    lateinit var myContext: Context

    internal lateinit var view: View

    private var timer: Timer? = null


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
    lateinit var phoneTV: TextView
    lateinit var save_pointTV: TextView


    var phone = ""
    var type = -1
    var save_point = ""
    var step = -1
    var member_id = -1
    var p_type = -1
    var new_member_yn = ""

    var stackpoint = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

        view = inflater.inflate(R.layout.fra_use,container,false)

        return inflater.inflate(R.layout.fra_use,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        phoneTV.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        save_point = save_pointTV.text.toString()

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
            if (text.length > 0){
                phoneTV.setText(text.substring(0, text.length - 1))
            }else{
            }
        }
        //checkStep()

        useLL.setOnClickListener {
            phone= phoneTV.text.toString()
            if (step == 1){
                step = 2
                loaduserdata()



            }else if (step ==4){
                step = 5
                loaduserdata()
            }
        }

    }
    // 프로세스
    fun changeStep() {
        val params = RequestParams()
        params.put("company_id", 1)
        params.put("member_id", member_id)
        params.put("new_member_yn", new_member_yn)
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
                        var data = response.getJSONObject("member")
                        var step = Utils.getInt(requestStep,"step")
//                        if (step ==2){
//                            timer!!.cancel()
//                        }

//                        step = Utils.getInt(requestStep, "step")

                        if (id < 0){
                            //아이디가 없음(비회원임)
                            changeStep()
                        } else {
                            //아이디값이 있는 경우(기존 회원임)
                            val params = RequestParams()
                            params.put("company_id", Utils.getString(data,"company_id"))

                            //회사 정보 조회
                            HttpClient.post("/company/info.json", params, object : JsonHttpResponseHandler() {
                                override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                                    if (progressDialog != null) {
                                        progressDialog!!.dismiss()
                                    }

                                    try {
                                        val result = response!!.getString("result")

                                        if ("ok" == result) {

                                            val company = response.getJSONObject("company")
                                            //val images = response.getJSONArray("images")//[]

                                            var comName = Utils.getString(company,"company_name")
                                            PrefUtils.setPreference(context, "storeName", Utils.getString(company,"company_name"))
                                            println("get storeName :: $comName")

                                        } else {

                                            /*val intent = Intent(context, LoginActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)*/

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
                                    //Utils.alert(context, "조회중 장애가 발생하였습니다.")
                                    Utils.alert(context, "조회중 장애가 발생했습니다.")
                                }

                                override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                                    if (progressDialog != null) {
                                        progressDialog!!.dismiss()
                                    }

                                    // System.out.println(responseString);

                                    throwable.printStackTrace()
                                    error()
                                }

                                override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                                    if (progressDialog != null) {
                                        progressDialog!!.dismiss()
                                    }

                                    // print(errorResponse)

                                    throwable.printStackTrace()
                                    error()
                                }

                                override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                                    if (progressDialog != null) {
                                        progressDialog!!.dismiss()
                                    }

                                    // print(errorResponse)

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
                            //회사 정보 조회

                            println("상호명 ::::::: ${PrefUtils.getStringPreference(context,"storeName")}")


                            changeStep()
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
                        val result_step = Utils.getInt(requestStep, "step")
                        if(step != result_step) {
                            step = result_step
                            Log.d("스텝",step.toString())
                            if (step == 3) {

                                timer!!.cancel()
                                phonET.setHint("사용할 포인트를 입력하세요.")
                                titleTV.text = "쿠폰/포인트\n조회"
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
        params.put("company_id", 1)
        params.put("phone", phone)

        MemberAction.is_member(params, object : JsonHttpResponseHandler() {
        //RequestStepAction.change_step(params, object : JsonHttpResponseHandler() {

            @SuppressLint("ResourceType")
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    Log.d("포인트",response.toString())
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        /*var requestStep = response.getJSONObject("RequestStep")
                        var step = Utils.getInt(requestStep,"step")
                        if (step ==3){
                            timer!!.cancel()
                        }*/
                        var isNewMember = response!!.getString("new_member_yn")

                        if (isNewMember.equals("N")){
                            var phone = response!!.getString("phone")
                            //Point_Use_Fragment로 유저 id랑 company id를 넘김
                            //Activity의 Intent
                            /*val frag = Point_Use_Fragment()
                            val bundle = Bundle()
                            bundle.putString("phone", phone)
                            frag.arguments = bundle


                            val fragManager = fragmentManager
                            val fragTrans = fragManager?.beginTransaction()
                            fragTrans?.replace(R.layout.fra_coupon, frag)
                            fragTrans?.commit()*/

                            //fourLL.callOnClick()

                            /*var sendItt = Intent()
                            sendItt.action = "USER_PHONE_NUMBER"
                            sendItt.putExtra("phone", phone)
                            context?.sendBroadcast(sendItt)*/


                        }


                    }else{
                        Toast.makeText(myContext,"조회실패",Toast.LENGTH_SHORT).show()
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


}
