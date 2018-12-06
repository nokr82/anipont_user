package com.devstories.aninuriandroid.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.devstories.aninuriandroid.R
import kotlinx.android.synthetic.main.activity_coupon_use.*

class UseActivity : FragmentActivity() {
    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var type = -1
    var request_step_id = -1
    var save_point: String? = null

    val UseFragment = UseFragment()
    val Point_Use_Fragment = Point_Use_Fragment()
    val Point_AccurMulaage_Fragment = Point_AccurMulaage_Fragment()

    internal var getPhoneNumber: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {

                setmenu()
                type = intent.getIntExtra("type", -1)
                var phoneNumber = intent.getStringExtra("phone")

                val bundle = Bundle()
                bundle.putString("phoneNumber", phoneNumber)
                bundle.putString("type", "STEP")
                Point_Use_Fragment.arguments = bundle

                phonET.setHint("사용할 포인트를 입력하세요.")
                titleTV.text = "쿠폰/포인트\n조회"

                use_op_LL.visibility = View.GONE
                couponLL.setBackgroundResource(R.drawable.background_strock_707070)
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, Point_Use_Fragment).commit()

            }
        }
    }

    internal var myPointReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {

                setmenu()
                val type = intent.getStringExtra("type")
                var phoneNumber = intent.getStringExtra("phone")

                val bundle = Bundle()
                bundle.putString("phoneNumber", phoneNumber)
                bundle.putString("type", type)
                Point_Use_Fragment.arguments = bundle

                titleTV.text = "쿠폰/포인트\n조회"

                use_op_LL.visibility = View.GONE
                couponLL.setBackgroundResource(R.drawable.background_strock_707070)
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, Point_Use_Fragment).commit()

            }
        }
    }

    internal var endRequestStepReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            var intent1 = Intent()
            intent1.putExtra("request_step_id", request_step_id)
            setResult(RESULT_OK, intent1)

            finish()
        }
    }

    internal var finishActivityReceiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_use)

        this.context = this
        progressDialog = ProgressDialog(context)


        //인텐트로 전화번호를 받는다
        //전화번호로 고객의 정보를 조회하고
        val filter = IntentFilter("POINT_USE")
        registerReceiver(getPhoneNumber, filter)

        val filter1 = IntentFilter("END_STEP")
        registerReceiver(endRequestStepReceiver, filter1)

        val filter2 = IntentFilter("FINISH_ACTIVITY")
        registerReceiver(finishActivityReceiver, filter2)

        val filter3 = IntentFilter("MY_POINT")
        registerReceiver(myPointReceiver, filter3)

        intent = getIntent()
        save_point = intent.getStringExtra("save_point")
        type = intent.getIntExtra("type", -1)
        request_step_id = intent.getIntExtra("request_step_id", -1)


        if (type == 1) {
            useLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, UseFragment).commit()
            use_op_LL.visibility = View.VISIBLE
        } else if (type == 2) {
            setmenu()
            phonET.setHint("사용할 포인트를 입력하세요.")
            titleTV.text = "쿠폰/포인트\n조회"
//            use_op_LL.visibility = View.GONE
//            couponLL.setBackgroundResource(R.drawable.background_strock_707070)
//            supportFragmentManager.beginTransaction().replace(R.id.main_frame, Point_Use_Fragment).commit()

            val bundle = Bundle()
            bundle.putInt("type", type)
            UseFragment.arguments = bundle

            useLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, UseFragment).commit()
            use_op_LL.visibility = View.VISIBLE

        } else {
            useLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, Point_AccurMulaage_Fragment).commit()
            use_op_LL.visibility = View.VISIBLE
        }

        useLL.setOnClickListener {
            setmenu()
            phonET.setHint("휴대전화번호를 눌러주세요")
            titleTV.text = "사용/조회"
            use_op_LL.visibility = View.VISIBLE
            useLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, UseFragment).commit()
        }





        val frgMng = supportFragmentManager
        val useFrag = UseFragment()


    }

    fun setmenu() {
        couponLL.setBackgroundResource(R.drawable.background_strock_null)
        useLL.setBackgroundResource(R.drawable.background_strock_null)
    }


    override fun onDestroy() {
        super.onDestroy()

        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }

        if (getPhoneNumber != null) {
            unregisterReceiver(getPhoneNumber)
        }

        if (endRequestStepReceiver != null) {
            unregisterReceiver(endRequestStepReceiver)
        }

        if (finishActivityReceiver != null) {
            unregisterReceiver(finishActivityReceiver)
        }

        if (myPointReceiver != null) {
            unregisterReceiver(myPointReceiver)
        }

    }

}


