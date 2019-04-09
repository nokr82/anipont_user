package com.devstories.aninuriandroid.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.devstories.aninuriandroid.Actions.CompanyAction
import com.devstories.aninuriandroid.Actions.RequestStepAction
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.*
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_contract_write.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.*

//로그인
class ContractWriteActivity : RootActivity() {
    lateinit var context: Context
    private var progressDialog: CustomProgressDialog? = null
    lateinit var adapter: ArrayAdapter<String>
    var option_amount = ArrayList<String>()
    var categoryIndex = ArrayList<Int>()
    var bitmap: BitmapDrawable? = null
    private val GALLERY = 1
    var year: Int = 1
    var month: Int = 1
    var day: Int = 1
    var company_id = -1
    var page: Int = 1
    var totalpage: Int = 1
    var confirm_num = ""
    var category_id = -1
    var phone = ""
    var contract_id = -1
    var addImages = ArrayList<Bitmap>()


    var request_step_id = -1
    private val SIGN_UP = 1215
    internal var reloadReciver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                contract_id = intent.getIntExtra("contract_id",-1)
                contract_detail()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiOptions = window.decorView.systemUiVisibility
        hideNavigations(this)
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
        }
        setContentView(R.layout.activity_contract_write)

        this.context = this
        progressDialog = CustomProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        company_id = PrefUtils.getIntPreference(context, "company_id")

        //날짜갖고오기
        var calendar = GregorianCalendar()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        var filter1 = IntentFilter("SIGNUP")
        context.registerReceiver(reloadReciver, filter1)
        contract_list()

        contract_id = intent.getIntExtra("contract_id",-1)

        Log.d("계약서",contract_id.toString())
        if (contract_id != -1){
            contract_detail()
        }



        request_step_id = intent.getIntExtra("request_step_id",-1)
        if (request_step_id != -1){
            endStep(request_step_id)
        }

        contractSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                category_id = categoryIndex[position]
                Log.d("카테", category_id.toString())
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        confirmTV.setOnClickListener {
            contract_confirm()
        }

        signRL.setOnClickListener {
            val intent = Intent(context,DlgSignActivity::class.java)
            intent.putExtra("contract_id",contract_id)
            startActivityForResult(intent,SIGN_UP)
        }

        dateLL.setOnClickListener {
            datedlg()
        }

        writeTV.setOnClickListener {
            contract_write()
        }
        
    }

    fun contract_detail() {

        val params = RequestParams()
        params.put("contract_id", contract_id)

        CompanyAction.contract_detail(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    Log.d("작성", response.toString())

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        val contract_s = response!!.getJSONObject("contract")
                        val contract = contract_s.getJSONObject("Contract")
                        var sign_uri = Utils.getString(contract,"sign_uri")
                        val contract_imgs = response!!.getJSONArray("contract_imgs")
                        if (sign_uri != ""){
                            var image = Config.url + sign_uri
                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(image,signIV, Utils.UILoptionsUserProfile)
                        }
                        userLL.removeAllViews()
                        for(i in 0 until contract_imgs.length()) {
                            val image:JSONObject = contract_imgs[i] as JSONObject
                            val contractimage = image.getJSONObject("ContractImage")

                            val path = Config.url + Utils.getString(contractimage, "image_uri")
                            val userView = View.inflate(context, R.layout.item_contract_img, null)
                            val c_imgIV : ImageView = userView.findViewById(R.id.c_imgIV)
                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(path,c_imgIV, Utils.UILoptionsUserProfile)
                            userLL.addView(userView)

                        }


                    } else {

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

//                 System.out.println("오류!!!"+responseString);

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

    fun contract_write() {

        var memo = Utils.getString(memoET)
        var name = Utils.getString(nameET)
        var contract_date = Utils.getString(dateTV)
        var confirm_num2 = Utils.getString(confirmET)
        var email = Utils.getString(emailET)
        phone = Utils.getString(phoneET)


        if (category_id == -1){
            Toast.makeText(context,"계약서종류를 선택해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (phone==""||phone.length != 11){
            Toast.makeText(context,"연락처를 올바르게 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (email==""){
            Toast.makeText(context,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (name==""){
            Toast.makeText(context,"성함을 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (contract_date==""||contract_date=="날짜 선택"){
            Toast.makeText(context,"날짜를 선택해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if (confirm_num != confirm_num2){
            Toast.makeText(context,"인증번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return
        }

        if (contract_id==-1){
            Toast.makeText(context,"서명을 해주세요.",Toast.LENGTH_SHORT).show()
            return
        }


        val params = RequestParams()



        params.put("id", contract_id)
        params.put("company_id", company_id)
        params.put("phone", phone)
        params.put("name", name)
        params.put("memo", memo)
        params.put("email", email)

        params.put("confirm_num", confirm_num2)
        params.put("contract_id", category_id)
        params.put("contract_date", contract_date)




        CompanyAction.contract_write(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    Log.d("작성", response.toString())

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        Toast.makeText(context,"작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                        finish()
                    } else {

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

//                 System.out.println("오류!!!"+responseString);

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


    fun contract_confirm() {
        phone = Utils.getString(phoneET)

        if (phone==""||phone.length != 11){
            Toast.makeText(context,"연락처를 올바르게 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        val params = RequestParams()
        params.put("company_id", company_id)
        params.put("phone", phone)

        CompanyAction.contract_confirm(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    Log.d("인증", response.toString())

                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        confirm_num = response!!.getString("confirm_num")
                        Toast.makeText(context,"발송완료",Toast.LENGTH_SHORT).show()

                    } else {

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




    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)
        dateTV.text = msg
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)

    }

    fun datedlg() {
        DatePickerDialog(context, dateSetListener, year, month, day).show()
    }

    fun contract_list() {
        val params = RequestParams()
        params.put("company_id", company_id)

        CompanyAction.contract_list(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    Log.d("계약", response.toString())

                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var data = response.getJSONArray("contract")
                        option_amount.clear()
                        option_amount.add("종류선택")
                        categoryIndex.add(-1)
                        for (i in 0 until data.length()) {
                            var json = data[i] as JSONObject
                            var type = json.getJSONObject("ContractType")
                            var name = Utils.getString(type, "name")
                            option_amount.add(name)
                            val category_id = Utils.getInt(type, "id")
                            categoryIndex.add(category_id)
                        }

                        adapter = ArrayAdapter(context, R.layout.spiner_item, option_amount)
                        contractSP.adapter = adapter

                        adapter.notifyDataSetChanged()

                    } else {

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


    override fun onPause() {
        super.onPause()
    }


    override fun onResume() {
        super.onResume()
        hideNavigations(this)
    }
    fun hideNavigations(context: Activity) {
        val decorView = context.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
    // 요청 삭제
    fun endStep(request_step_id:Int) {
        val params = RequestParams()
        params.put("request_step_id", request_step_id)

        RequestStepAction.end_step(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {

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

        })
    }


    override fun onDestroy() {
        super.onDestroy()

        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }

    }


}
