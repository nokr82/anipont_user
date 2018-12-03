package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.devstories.aninuriandroid.R
import org.json.JSONObject
import java.util.ArrayList

open class CouponListAdapter (context: Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data)  {
    private lateinit var item : ViewHolder
    var view : Int = view
    var data: ArrayList<JSONObject> = data

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
        } else {
            retView = convertView
            item = convertView.tag as ViewHolder
            if (item == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            }
        }

        var json = data.get(position)
        val coupons = json.getJSONArray("coupons")
        //val coupon = coupons.getJSONObject("Coupon")
        return retView
    }

    override fun getItem(position: Int): JSONObject {
        return data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.count()
    }

    class ViewHolder(v : View) {
        var item_couponNameTV :TextView
        var item_couponTypeTV :TextView
        var item_messageTV :TextView
        var item_weekTV :TextView
        var item_satTV :TextView
        var item_sunTV :TextView
        var item_validityTV :TextView


        init {
            item_couponNameTV = v.findViewById(R.id.item_couponNameTV)
            item_couponTypeTV = v.findViewById(R.id.item_couponTypeTV)
            item_messageTV = v.findViewById(R.id.item_messageTV)
            item_weekTV = v.findViewById(R.id.item_weekTV)
            item_satTV = v.findViewById(R.id.item_satTV)
            item_sunTV = v.findViewById(R.id.item_sunTV)
            item_validityTV = v.findViewById(R.id.item_validityTV)
        }
    }
}