package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class UseFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null
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
    lateinit var phoneTV: TextView
    lateinit var save_pointTV: TextView


    var phone = -1

    var type = -1
    var save_point = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        progressDialog = ProgressDialog(myContext)

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

        useLL.setOnClickListener {
            var phone = phoneTV.text.toString()

            loaduserdata(phone)
        }

    }

    fun loaduserdata(phone:String) {
        val params = RequestParams()
        params.put("phone", phone)

        MemberAction.my_info(params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    Log.d("포인트",response.toString())
                    val result = response!!.getString("result")
                    if ("ok" == result) {
                        var data = response.getJSONObject("member")
                        var member = data.getJSONObject("Member")
                        var id  = Utils.getString(member,"id")

                        stack_point(id)
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

    fun stack_point(member_id:String) {
        val params = RequestParams()
        params.put("member_id",member_id)
        params.put("company_id", 1)
        params.put("stack_point", save_point)

        MemberAction.point_stack(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                try {
                    val result = response!!.getString("result")
                    Log.d("적립",response.toString())

                    if ("ok" == result) {
                        type = 3
                        val intent = Intent(myContext, UseActivity::class.java)
                        intent.putExtra("type",type)
                        intent.putExtra("phone",phone)
                        intent.putExtra("save_point",save_point)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
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
