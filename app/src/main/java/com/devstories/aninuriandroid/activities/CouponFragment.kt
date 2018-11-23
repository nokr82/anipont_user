package com.devstories.aninuriandroid.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.aninuriandroid.Actions.MemberAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.PrefUtils
import com.devstories.aninuriandroid.base.Utils
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.nostra13.universalimageloader.core.ImageLoader
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fra_coupon.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CouponFragment : Fragment() {
    lateinit var myContext: Context
    private var progressDialog: ProgressDialog? = null
    var left_point = ""
    var point = ""
    var use_point = ""


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
    lateinit var left_pointTV: TextView
    lateinit var pointTV: TextView
    lateinit var use_pointTV: TextView




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context

            return inflater.inflate(R.layout.fra_coupon,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        loadpoint("01041145671")

        l_point()

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
            if (text.length > 0){
                use_pointTV.setText(text.substring(0, text.length - 1))
                if (!text.equals("")){
                    l_point()
                }
            }
        }

    }

    fun l_point(){
        point = pointTV.text.toString()
        use_point = use_pointTV.text.toString()
        left_point = left_pointTV.text.toString()

        val numpoint = Integer.parseInt(point)
        var n_use_point =Integer.parseInt(use_point)
        var n_left_point = Integer.parseInt(left_point)

            n_left_point = numpoint - n_use_point

        pointTV.text = numpoint.toString()
        use_pointTV.text = n_use_point.toString()
        left_pointTV.text = n_left_point.toString()
    }
//포인트조회
fun loadpoint(phone:String) {
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
                    var point  = Utils.getString(member,"point")

                    pointTV.text = point

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
