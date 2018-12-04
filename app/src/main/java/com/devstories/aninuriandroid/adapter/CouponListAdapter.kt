package com.devstories.aninuriandroid.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.devstories.aninuriandroid.R
import com.devstories.aninuriandroid.base.Utils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList

open class CouponListAdapter (context: Context, view:Int, data: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, view, data)  {
    private lateinit var item : ViewHolder
    var view : Int = view
    var data: ArrayList<JSONObject> = data

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
            item = ViewHolder(retView)
            retView.tag = item
        } else {
            retView = convertView
            item = convertView.tag as ViewHolder
            if (item == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            }
        }

        val couponOJ = data.get(position)

        val memberCoupon = couponOJ.getJSONObject("MemberCoupon")
        val coupon = couponOJ.getJSONObject("Coupon")

        val used = Utils.getString(memberCoupon, "use_yn")
        val del = Utils.getString(memberCoupon, "del_yn")

        if (used == "N" && del == "N") {

            val coupon_name = Utils.getString(coupon, "name")
            val coupon_type = Utils.getString(coupon, "type")
            val coupon_valid = SimpleDateFormat("yyyy-MM-dd")
                    .parse(Utils.getString(memberCoupon, "e_use_date"))
            val coupon_week = Utils.getString(coupon, "week_use_yn")
            val coupon_sat = Utils.getString(coupon, "sat_use_yn")
            val coupon_sun = Utils.getString(coupon, "sun_use_yn")


        }

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