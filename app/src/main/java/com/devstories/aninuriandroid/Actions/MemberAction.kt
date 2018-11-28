package com.devstories.aninuriandroid.Actions

import com.devstories.aninuriandroid.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object MemberAction {

    // 회원 페이지
    fun my_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_info.json", params, handler)
    }

    //포인트적립
    fun point_stack(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/point_stack.json", params, handler)
    }
}