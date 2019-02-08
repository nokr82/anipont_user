package com.devstories.aninuriandroid.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.devstories.aninuriandroid.R
import kotlinx.android.synthetic.main.activity_coupon_use.*

class UseActivity : FragmentActivity() {
    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var type = -1
    var request_step_id = -1
    var save_point: String? = null

    val UseFragment = UseFragment()
    val SelectFragment = SelectFragment()
    val StackFragment = StackFragment()
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
                Point_Use_Fragment.arguments = bundle

                phonET.setHint("사용할 포인트를 입력하세요.")
                titleTV.text = "쿠폰/포인트\n사용"

                use_op_LL.visibility = View.GONE
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
        setContentView(R.layout.activity_coupon_use)

        this.context = this
        progressDialog = ProgressDialog(context)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        //인텐트로 전화번호를 받는다
        //전화번호로 고객의 정보를 조회하고
        val filter = IntentFilter("POINT_USE")
        registerReceiver(getPhoneNumber, filter)

        val filter1 = IntentFilter("END_STEP")
        registerReceiver(endRequestStepReceiver, filter1)

        val filter2 = IntentFilter("FINISH_ACTIVITY")
        registerReceiver(finishActivityReceiver, filter2)

        intent = getIntent()
        save_point = intent.getStringExtra("save_point")
        type = intent.getIntExtra("type", -1)
        request_step_id = intent.getIntExtra("request_step_id", -1)


        if (type == 1) {
            titleTV.text = "포인트 적립"
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, UseFragment).commit()
            use_op_LL.visibility = View.GONE
        } else if (type == 2) {
            setmenu()
            phonET.setHint("사용할 포인트를 입력하세요.")
            titleTV.text = "쿠폰/포인트\n조회"

            val bundle = Bundle()
            bundle.putInt("type", type)
            SelectFragment.arguments = bundle

            supportFragmentManager.beginTransaction().replace(R.id.main_frame, SelectFragment).commit()
            use_op_LL.visibility = View.GONE
        }else if (type == 3) {
            titleTV.text = "쿠폰/포인트\n사용"
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, StackFragment).commit()
            use_op_LL.visibility = View.GONE
        } else {
            titleTV.text = "적립/사용\n완료"
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, Point_AccurMulaage_Fragment).commit()
            use_op_LL.visibility = View.GONE
            titleLL.visibility = View.GONE
            usetitle_op_LL.visibility = View.GONE
        }

      /*  useLL.setOnClickListener {
            setmenu()
            phonET.setHint("휴대전화번호를 눌러주세요")
            titleTV.text = "사용/조회"
            use_op_LL.visibility = View.VISIBLE
            useLL.setBackgroundResource(R.drawable.background_strock_707070)
            supportFragmentManager.beginTransaction().replace(R.id.main_frame, UseFragment).commit()
        }*/



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

    }

}


