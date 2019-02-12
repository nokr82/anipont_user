package com.devstories.aninuriandroid.Actions

import com.devstories.aninuriandroid.base.HttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams


/**
 * Created by hooni
 */
object MemberAction {

    // 로그인
    fun login(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/login/index.json", params, handler)
    }
    // 로그인
    fun membership_point(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/membership_point.json", params, handler)
    }

    // 업체 정보
    fun company_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/company/info.json", params, handler)
    }

    // 회원 페이지
    fun my_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_info.json", params, handler)
    }

    //포인트적립
    fun point_stack(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/point_stack.json", params, handler)
    }

    //멤버체크
    fun is_member(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/is_member.json", params, handler)
    }

    //사용자 포인트 조회
    fun inquiry_point(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_point.json", params, handler)
    }

}