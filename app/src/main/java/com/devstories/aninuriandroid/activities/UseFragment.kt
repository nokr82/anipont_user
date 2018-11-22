package com.devstories.aninuriandroid.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.devstories.aninuriandroid.R
import kotlinx.android.synthetic.main.fra_use.*


class UseFragment : Fragment() {
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
    var type = -1
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.myContext = container!!.context
        return inflater.inflate(R.layout.fra_use,container,false)
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
        useLL = view.findViewById(R.id.useLL)

        phoneTV = view.findViewById(R.id.phoneTV)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        phoneTV.addTextChangedListener(PhoneNumberFormattingTextWatcher())


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
            type = 3
            val intent = Intent(myContext, UseActivity::class.java)
            intent.putExtra("type",type)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }
}
